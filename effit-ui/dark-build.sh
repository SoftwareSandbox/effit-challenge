npm run dark
read -p "Username: " uname
stty -echo
read -p "Password: " passw; echo
stty echo
./dark-cli-apple --canvas $uname-effit --user $uname --password $passw dist
