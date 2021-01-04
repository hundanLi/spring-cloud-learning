curl --request GET \
     --url 'http://localhost:10000'\
     --header 'Cookie:mycookie=myvalue'

echo ''

curl --request GET \
     --url 'http://localhost:10000'\
     --header 'Cookie:mycookie=othervalue'

echo ''