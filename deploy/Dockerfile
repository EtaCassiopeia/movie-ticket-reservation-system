FROM nginx:latest
ENV CONSUL_URL consul:8500
RUN apt-get update && apt-get install -y unzip wget
RUN mkdir -p /opt/packt/consul-template
WORKDIR /opt/packt/consul-template
RUN wget https://releases.hashicorp.com/consul-template/0.18.2/consul-template_0.18.2_linux_amd64.zip && unzip consul-template_0.18.2_linux_amd64.zip
ADD default.ctmpl /opt/packt/consul-template/
ADD startup.sh /opt/packt/consul-template/
RUN chmod +777 /opt/packt/consul-template/startup.sh
RUN rm /etc/nginx/conf.d/default.conf
EXPOSE 80
ENTRYPOINT ["/opt/packt/consul-template/startup.sh"]
