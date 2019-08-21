echo "${{ secrets.HEROKU_AUTH_TOKEN }}" | docker login --username="${{ secrets.HEROKU_USERNAME }}" --password-stdin registry.heroku.com;
docker build ./ops/webapp -t swsb/effitchallenge;
docker tag swsb/effitchallenge registry.heroku.com/effit-challenge/web;
docker push registry.heroku.com/effit-challenge/web;
