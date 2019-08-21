docker build ./ops/webapp -t swsb/effitchallenge;
docker tag swsb/effitchallenge registry.heroku.com/effit-challenge/web;
docker push registry.heroku.com/effit-challenge/web;
