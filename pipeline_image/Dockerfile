FROM debian:12
RUN \
    apt update && apt upgrade \
    && apt-get install sudo
ENV HOME=/opt
WORKDIR $HOME
ADD openjdk-17_linux-x64_bin.tar.gz $HOME
ADD "apache-maven-3.8.6-bin.tar.gz" $HOME
ADD jdk_home.sh /etc/profile.d/
ADD maven_home.sh /etc/profile.d/
RUN chmod -x /etc/profile.d/jdk_home.sh \
    && chmod -x /etc/profile.d/maven_home.sh
RUN sudo apt-get install apt-transport-https ca-certificates gnupg curl sudo -y\
    && curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo gpg --dearmor -o /usr/share/keyrings/cloud.google.gpg \
    && echo "deb [signed-by=/usr/share/keyrings/cloud.google.gpg] https://packages.cloud.google.com/apt cloud-sdk main" | sudo tee -a /etc/apt/sources.list.d/google-cloud-sdk.list \
    && sudo apt-get update && sudo apt-get install google-cloud-cli \
    && sudo apt-get install google-cloud-cli-docker-credential-gcr
ENV LANG=C.UTF-8 \
    PATH="/opt/apache-maven-3.8.6/bin:/opt/jdk-17/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"
CMD ["mvn"]

