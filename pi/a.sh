
sudo ps -ef | grep nginx | grep -v grep | awk '{print "sudo kill -9 " $2}' | sh

cp /media/pi/k/g1 /etc/nginx/sites-enabled/
cp /media/pi/k/h /etc/nginx/sites-enabled/
cp /media/pi/k/i /etc/nginx/sites-enabled/
cp /media/pi/k/j /etc/nginx/sites-enabled/
cp /media/pi/k/default /etc/nginx/sites-enabled/

#sudo ps -ef | grep nginx | grep -v grep | awk '{print "sudo kill -9 " $2}' | sh

sudo nginx

cp /media/pi/k/smb.conf /etc/samba/

sudo service smbd restart


