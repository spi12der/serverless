#!/bin/bash
#Starting up a new VM
echo "Hello World"
if [ $# -ne 4 ]
then
	echo "Error in Number of Arguments"
	echo "Enter <IP> <USERNAME> <PASSWORD> of a physical machine followed by the <VM Name> to start on it"
	exit
else
	IP=$1
	username=$2
	password=$3
	mc_name=$4	
fi
sshpass -p $password ssh -o StrictHostKeyChecking=no  $username@$IP sh -s << EOSSH

#cd to Folder containing Vagrant File
cd vagrant_vm

vagrant up $mc_name 
#vagrant up

#cat temp.txt
vagrant ssh $mc_name -c "hostname -I | cut -d' ' -f2"
EOSSH


#To solve sudo problem
if false
then
ssh -t $HOST bash -c "'
ls

pwd

if true; then
    echo $HELLO
else
    echo "This is false"
fi

echo "Hello world"

sudo ls /root
'"
fi
