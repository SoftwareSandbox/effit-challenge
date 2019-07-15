
export WEB_DOCKER_IMAGE_ID="$(docker inspect swsb/effitchallenge --format={{.Id}})"
curl -n -X PATCH https://api.heroku.com/apps/effit-challenge/formation \
     --user $HEROKU_USERNAME:$HEROKU_AUTH_TOKEN \
     -H "Content-Type: application/json" \
     -H "Accept: application/vnd.heroku+json; version=3.docker-releases" \
     -d @- <<EOF
{
  "updates": [
    {
      "type": "web",
      "docker_image": "$WEB_DOCKER_IMAGE_ID"
    }
  ]
}
EOF