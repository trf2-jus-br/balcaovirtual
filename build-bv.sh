rm -R TRF2-SR-2017_NATO-TMP
mkdir TRF2-SR-2017_NATO-TMP

cd ../sistemaprocessual-api
mvn clean install -U
cd ../eproc-api
mvn clean install -U
cd ../apolo-api
mvn clean install -U
cd ../balcaovirtual
mvn clean install -U

mv ../balcaovirtual/target/balcaovirtual.war TRF2-SR-2017_NATO-TMP
cp ../eproc-api/target/eproc-api.war TRF2-SR-2017_NATO-TMP/eproc-api-sjrj.war
cp ../eproc-api/target/eproc-api.war TRF2-SR-2017_NATO-TMP/eproc-api-sjes.war
cp ../eproc-api/target/eproc-api.war TRF2-SR-2017_NATO-TMP/eproc-api-trf2.war
cp ../apolo-api/target/apolo-api.war TRF2-SR-2017_NATO-TMP/apolo-api-sjrj.war
cp ../apolo-api/target/apolo-api.war TRF2-SR-2017_NATO-TMP/apolo-api-sjes.war
cp ../apolo-api/target/apolo-api.war TRF2-SR-2017_NATO-TMP/apolo-api-trf2.war
echo 'Build Complete!'


