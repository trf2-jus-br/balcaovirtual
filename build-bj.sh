rm -R BJ-TMP
mkdir BJ-TMP

cd ../sistemaprocessual-api
mvn clean install -U
cd ../eproc-api
mvn clean install -U
cd ../apolo-api
mvn clean install -U
cd ../balcaojus
mvn clean install -U

cp ../balcaojus/target/balcaojus.war BJ-TMP/balcaojus.war
cp ../eproc-api/target/eproc-api.war BJ-TMP/eproc-api-sjrj.war
cp ../eproc-api/target/eproc-api.war BJ-TMP/eproc-api-sjes.war
cp ../eproc-api/target/eproc-api.war BJ-TMP/eproc-api-trf2.war
cp ../apolo-api/target/apolo-api.war BJ-TMP/apolo-api-sjrj.war
cp ../apolo-api/target/apolo-api.war BJ-TMP/apolo-api-sjes.war
cp ../apolo-api/target/apolo-api.war BJ-TMP/apolo-api-trf2.war
echo 'Build Complete!'


