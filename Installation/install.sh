# Suryansh Agnihotri
# sudo apt-get install cowsay
# cowsay "Namaste , installation process will begin in a second !"
echo "Namaste , installation process will begin in a second !"
/bin/sleep 5
# cowsay "Tom installation Started "
echo "Tom installation Started"


# starting tomcat

# . ./start_tomcat.sh


# Rabbit MQ Installation

# . ./install_rabbitmq.sh

function valid_ip()
{
    local  ip=$1
    local  stat=1

    if [[ $ip =~ ^[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}$ ]]; then
        OIFS=$IFS
        IFS='.'
        ip=($ip)
        IFS=$OIFS
        [[ ${ip[0]} -le 255 && ${ip[1]} -le 255 \
            && ${ip[2]} -le 255 && ${ip[3]} -le 255 ]]
        stat=$?
    fi
    return $stat
}

# cowsay "Enter the number of server available"
echo "Enter the number of server available (Minimum 4 )"
read available_server
while [ "$available_server" -lt 1 ]
do
	echo "please enter minimum 4 servers "
	read available_server
done
# cowsay "Please enter IP address , username and password "
echo "Please enter IP address , username and password "
cnt=0
list_of_server_ip=()
list_of_server_port=()
list_of_username=()
list_of_password=()

while [ "$cnt" -lt $available_server ]
do
	echo "server ip :"
	read server_ip
	echo "server port :"
	read server_port
	echo "username :"
	read username
	echo "password :"
	read password
	if valid_ip $server_ip; then stat='good'; else stat='bad';fi
	if [ "$stat" = "bad" ];then echo "please enter correct ip ";
	else
		cnt=`expr $cnt + 1`;
		list_of_password+=($password)
		list_of_username+=($username)
		list_of_server_ip+=($server_ip)
		list_of_server_port+=($server_port)
	fi
done

if [ -e server.xml ]
then 
	rm server.xml
fi

touch server.xml
index=0
echo "<?xml version="1.0"?>">>server.xml
echo "<root>">>server.xml
for i in "${!list_of_server_ip[@]}"; do
  echo "<server ip="${list_of_server_ip[$i]}" port="${list_of_server_port[$i]}" username="${list_of_username[$i]}" password="${list_of_password[$i]}" status="A" ></server>">>server.xml
done
echo "</root>">>server.xml



## 1st Server is Api Gateway




## 2nd Server is Broker
clear
sudo apt install sshpass
path='/home'
path=$path/${list_of_username[1]}/Desktop
chmod 777 ./rabbitmq_server.sh
sshpass -p ${list_of_password[1]} scp -r ./rabbitmq_server.sh ${list_of_username[1]}@${list_of_ip[1]}:$path
path=$path/rabbitmq_server.sh
sshpass -p ${list_of_password[1]} ssh -t ${list_of_username[1]}@${list_of_ip[1]} $path




## 3rd contain LoadBalancer ,ManageServer and ManageService LC

## make routing.xml

if [ -e routing.xml ]
then 
	rm routing.xml
fi
touch routing.xml
echo "<?xml version="1.0"?>">>routing.xml
echo "<root>">>routing.xml
echo  -e "\t<server cpu="50" status="up" ip = ${list_of_server_ip[0]} ></server>" >>routing.xml
echo -e "\t<server cpu="50" status="up" ip = ${list_of_server_ip[2]} >" >>routing.xml
echo -e "\t\t<service name="dataservice" containerid="container_dataservice" queue_name=${list_of_server_ip[2]}@dataservice></service>">>routing.xml
echo -e "\t\t<service name="manageserverlc" containerid="container_serverlc" queue_name=${list_of_server_ip[2]}@manageserverlc></service>">>routing.xml
echo -e "\t\t<service name="manageservicelc" containerid="container_servicelc" queue_name=${list_of_server_ip[2]}@manageservicelc></service>">>routing.xml
echo -e "\t\t<service name="fileservice" containerid="container_fileservice" queue_name=${list_of_server_ip[2]}@fileservice></service>">>routing.xml
echo -e "\t</server>">>routing.xml
echo "</root>">>routing.xml


# Docker work begins

#copy jar and routing.xml to corresponding server home directory and run serivices within docker

path='/home'/${list_of_username[2]}
load_balancer_jar_name="LoadBalancer.jar"
service_manager_jar_name="ServiceManager.jar"
server_manager_jar_name="ServerManager.jar"
chmod 777 ./routing.xml
chmod 777 ./$load_balancer_jar_name
chmod 777 ./$service_manager_jar_name
chmod 777 ./$server_manager_jar_name
sshpass -p ${list_of_password[0]} scp -r ./routing.xml ${list_of_username[0]}@${list_of_ip[0]}:$path
sshpass -p ${list_of_password[2]} scp -r ./$load_balancer_jar_name ${list_of_username[2]}@${list_of_ip[2]}:$path
sshpass -p ${list_of_password[2]} scp -r ./$service_manager_jar_name ${list_of_username[2]}@${list_of_ip[2]}:$path
sshpass -p ${list_of_password[2]} scp -r ./$server_manager_jar_name ${list_of_username[2]}@${list_of_ip[2]}:$path
sshpass -p ${list_of_password[2]} ssh -t ${list_of_username[2]}@${list_of_ip[2]} ' sudo service docker start'
sshpass -p ${list_of_password[2]} ssh -t ${list_of_username[2]}@${list_of_ip[2]} ' sudo docker run --name my2 --rm -v "$PWD":/tmp -w /tmp openjdk:8 java -jar ' $load_balancer_jar_name ' Hi Hello '
sshpass -p ${list_of_password[2]} ssh -t ${list_of_username[2]}@${list_of_ip[2]} ' sudo docker run --name my2 --rm -v "$PWD":/tmp -w /tmp openjdk:8 java -jar ' $server_manager_jar_name ' Hi Hello '
sshpass -p ${list_of_password[2]} ssh -t ${list_of_username[2]}@${list_of_ip[2]} ' sudo docker run --name my2 --rm -v "$PWD":/tmp -w /tmp openjdk:8 java -jar ' $service_manager_jar_name ' Hi Hello '





## 4th and other are for extra service 










# for i in "${list_of_server_ip[@]}"
# do
#    : 
#    # do whatever on $i
#    echo "<server ip="$i" port="$list_of_server_port" username="rohit" password="123" status="A" />"
#    index=`expr $index + 1`;
# done


