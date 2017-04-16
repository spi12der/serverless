#!/bin/bash
#Find the ram of the machine and put a vagrantfile appropiately in that machine in directory vagrant_vm
if [ $# -ne 3 ]
then
        echo "Enter <FILE_NAME> <IP> <USERNAME> <PASSWORD>"
        exit
fi

vm () {
        mc_name=$1
        connection=$2
        #cat << "EOF" >> output.txt
        echo "
        config.vm.define \"mc$mc_name\" do |mc|
                mc.vm.box = \"3scale/docker\"
                if Vagrant.has_plugin?(\"vagrant-proxyconf\")
                        mc.proxy.http     = \"http://proxy.iiit.ac.in:8080/\"
                        mc.proxy.https    = \"http://proxy.iiit.ac.in:8080/\"
                        mc.proxy.no_proxy = \"localhost,127.0.0.1\"
                end
                mc.vm.network \"public_network\", bridge: \"$connection\"
        end" >>output.txt
}

createVagrantFile () {
	for (( c=1; c<=$1; c++ ))
        	do
        	        $(vm $c $2)
        	done
	cat .header.txt >Vagrantfile_new
	cat output.txt >>Vagrantfile_new
	cat .footer.txt >>Vagrantfile_new
	rm output.txt
}

IP=$1
username=$2
password=$3
directory_name="vagrant_vm"
sshpass -p $password ssh -o StrictHostKeyChecking=no $username@$IP "mkdir $directory_name"
memory_size=$(sshpass -p $password ssh -o StrictHostKeyChecking=no $username@$IP "grep MemTotal /proc/meminfo| tr -s ' '|cut -d' ' -f2")
connection_name=$(sshpass -p $password ssh -o StrictHostKeyChecking=no $username@$IP "ip route ls| head -1| tr -s ' '| cut -d' ' -f5")
default_size=2097152
no_of_VMS=$(printf "%.*f\n" 0 `echo "$memory_size / $default_size" | bc -l`)
$(createVagrantFile $no_of_VMS $connection_name)	
sshpass -p $password scp Vagrantfile_new $username@$IP:~/$directory_name/Vagrantfile
rm Vagrantfile_new
echo $no_of_VMS
