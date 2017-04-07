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
echo "Enter the number of server available"
read available_server
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
  # printf "%s\t%s\n" "$i" "${foo[$i]}"
  echo "<server ip="${list_of_server_ip[$i]}" port="${list_of_server_port[$i]}" username="${list_of_username[$i]}" password="${list_of_password[$i]}" status="A" />">>server.xml
done
echo "</root>">>server.xml

# for i in "${list_of_server_ip[@]}"
# do
#    : 
#    # do whatever on $i
#    echo "<server ip="$i" port="$list_of_server_port" username="rohit" password="123" status="A" />"
#    index=`expr $index + 1`;
# done


