#!/bin/bash
#Find the ram of the machine and put a vagrantfile appropiately in that machine in directory vagrant_vm
if [ $# -ne 1 ]
then
        echo "Enter <FILE_NAME> containing IPs of all Physical Machines"
        exit
fi
filename="$1"
while read -r line
do
        IP=$(echo $line | awk '{print $1}')
        username=$(echo $line | awk '{print $2}')
        password=$(echo $line | awk '{print $3}')
	bash SingleMachineSetup.sh $IP $username $password
done < "$filename"
