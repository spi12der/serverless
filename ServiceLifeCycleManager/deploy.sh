# $1 $2 $3 $4 $5 $6 $7 $8
#cmd="sudo docker run --name my1 --rm -v '$PWD':/tmp -w /tmp openjdk:7 java -jar $4 $5 $6 $7 $8"
cmd="nohup java -jar $4 $5 $6 $7 $8 > out.txt 2>&1 &"
sshpass -p $3 ssh -o "StrictHostKeyChecking=no" $2@$1 "$cmd"
