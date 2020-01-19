npm run dark
sed -i .bak 's/\/DARK_STATIC_ASSETS_BASE_URL/DARK_STATIC_ASSETS_BASE_URL/g' dist/index.html
read -p "Username: " uname
stty -echo
read -p "Password: " passw; echo
stty echo
./dark-cli-apple --canvas $uname-effit --user $uname --password $passw dist
