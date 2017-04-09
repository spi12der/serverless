#!/bin/bash

$CATALINA_HOME/bin/shutdown.sh
#stop tomcat if already running 
# get the process id running at port 8080
var=$(sudo netstat -nlp | grep 8080 | tr -s ' ' | cut -d' ' -f 7 | cut -d'/' -f 1)
var=$(echo $var | wc -m)

if [  $var -gt 1 ] 
	then
	
if [ -n "$previousDir" ]
	then
	echo $CATALINA_HOME
	"$CATALINA_HOME"/bin/shutdown.sh
	#sudo rm -r $CATALINA_HOME
fi
fi

#remove tomcat if present in opt package
if [ -d "/opt/tomcat2" ]
	then
sudo rm -r /opt/tomcat2
fi

#untar the tomcat package
`tar -xvf apache-tomcat-8.5.11.tar.gz &>/dev/null`

# move the tomcat to /opt/tomcat
`sudo mv apache-tomcat-8.5.11 /opt/tomcat2`

#set the permission for using tomcat
`sudo chmod -R  777 /opt/tomcat2`

# setting the environment variable for this shell
export CATALINA_HOME=/opt/tomcat2 

#copy project to apache working directory (/webapps/)
`sudo cp Serverless.war $CATALINA_HOME/webapps/`
`sudo chmod 777 $CATALINA_HOME/webapps/Serverless.war`
#start the serverless
$CATALINA_HOME/bin/startup.sh
