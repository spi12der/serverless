sshpass -p $3 ssh -o "StrictHostKeyChecking=no" $2@$1 "sshpass -p $6 scp $7 $5@$4:~"
