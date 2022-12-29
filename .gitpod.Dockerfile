FROM gitpod/workspace-full-vnc

USER gitpod

ENV ANDROID_SDK_ROOT /opt/android-sdk-linux/sdk

# ------------------------------------------------------
# --- Install required tools

RUN sudo add-apt-repository ppa:openjdk-r/ppa
RUN sudo dpkg --add-architecture i386

# Dependencies to execute Android builds
RUN sudo apt-get update -qq
RUN DEBIAN_FRONTEND=noninteractive sudo apt-get install -y openjdk-8-jdk openjdk-11-jdk libc6:i386 libstdc++6:i386 libgcc1:i386 libncurses5:i386 libz1:i386 net-tools

# Keystore format has changed since JAVA 8 https://bugs.launchpad.net/ubuntu/+source/openjdk-9/+bug/1743139
RUN sudo mv /etc/ssl/certs/java/cacerts /etc/ssl/certs/java/cacerts.old \
    && sudo keytool -importkeystore -destkeystore /etc/ssl/certs/java/cacerts -deststoretype jks -deststorepass changeit -srckeystore /etc/ssl/certs/java/cacerts.old -srcstoretype pkcs12 -srcstorepass changeit \
    && sudo rm /etc/ssl/certs/java/cacerts.old

# Select JAVA 11  as default
RUN sudo update-alternatives --set javac /usr/lib/jvm/java-11-openjdk-amd64/bin/javac
RUN sudo update-alternatives --set java /usr/lib/jvm/java-11-openjdk-amd64/bin/java

# ------------------------------------------------------
# --- Download Android Command line Tools into $ANDROID_SDK_ROOT

RUN cd /opt \
    && sudo wget -q https://dl.google.com/android/repository/commandlinetools-linux-6858069_latest.zip -O android-commandline-tools.zip \
    && sudo mkdir -m=rwx -p ${ANDROID_SDK_ROOT}/cmdline-tools \
    && unzip -q android-commandline-tools.zip -d /tmp/ \
    && sudo mv /tmp/cmdline-tools/ ${ANDROID_SDK_ROOT}/cmdline-tools/latest \
    && sudo rm android-commandline-tools.zip && ls -la ${ANDROID_SDK_ROOT}/cmdline-tools/latest/

ENV PATH ${PATH}:${ANDROID_SDK_ROOT}/platform-tools:${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin

# ------------------------------------------------------
# --- Install Android SDKs and other build packages

# Other tools and resources of Android SDK
#  you should only install the packages you need!
# To get a full list of available options you can use:
#  sdkmanager --list

# Accept licenses before installing components, no need to echo y for each component
# License is valid for all the standard components in versions installed from this file
# Non-standard components: MIPS system images, preview versions, GDK (Google Glass) and Android Google TV require separate licenses, not accepted there
RUN yes | sdkmanager --licenses

RUN touch .android/repositories.cfg

# Platform tools
RUN yes | sudo env PATH=$PATH sdkmanager "platform-tools"

# SDKs

RUN yes | sdkmanager --update --channel=0
# Please keep all sections in descending order!
RUN yes | sudo env PATH=$PATH sdkmanager \
    "platforms;android-33" \
    "platforms;android-32" \
    "platforms;android-31" \
    "platforms;android-30" \
    "build-tools;33.0.1" \
    "build-tools;33.0.0" \
    "build-tools;32.0.0" \
    "build-tools;31.0.0" \
    "build-tools;30.0.3" \
    "build-tools;30.0.2" \
    "build-tools;30.0.1" \
    "build-tools;30.0.0" \
    "extras;android;m2repository" \
    "extras;google;m2repository" \
    "extras;google;google_play_services"

# ------------------------------------------------------
# --- Install Gradle from PPA

# Gradle PPA
ENV GRADLE_VERSION=7.4.2
ENV PATH=$PATH:"/opt/gradle/gradle-7.4.2/bin/"
RUN sudo wget https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -P /tmp \
    && sudo unzip -d /opt/gradle /tmp/gradle-*.zip \
    && sudo chmod +775 /opt/gradle \
    && gradle --version \
    && sudo rm -rf /tmp/gradle*

# ------------------------------------------------------
# --- Install Maven 3 from PPA

RUN sudo apt-get purge maven maven2 \
 && sudo apt-get update \
 && sudo apt-get -y install maven \
 && mvn --version

ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-amd64

# ------------------------------------------------------
# --- Cleanup and rev num

# Cleaning
RUN sudo apt-get clean
