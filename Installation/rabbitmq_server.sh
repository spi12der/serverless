
touch password
echo $1 >> password 
echo 'deb http://www.rabbitmq.com/debian/ testing main' |
     sudo -S sh -c "touch /etc/apt/sources.list.d/rabbitmq.list"<password  
     
wget -O- https://www.rabbitmq.com/rabbitmq-release-signing-key.asc |
     sudo  apt-key add - 
echo $1 | sudo -S apt-get update
sudo -S apt-get install rabbitmq-server < password
sudo -S invoke-rc.d rabbitmq-server start < password
sudo rabbitmqctl add_user test test
sudo rabbitmqctl set_user_tags test administrator
sudo rabbitmqctl set_permissions -p / test ".*" ".*" ".*"
sudo -S invoke-rc.d rabbitmq-server restart < password
rm password
