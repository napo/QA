DB="../../db.sqlite"
wget -c http://www.istat.it/storage/cartografia/confini_amministrativi/non_generalizzati/2016/Limiti_2016_WGS84.zip
unzip Limiti_2016_WGS84.zip
cd Limiti_2016_WGS84
cd Com2016_WGS84
spatialite_tool -i -shp Com2016_WGS84 -d $DB -t adm3 -g geometry -c UTF-8
cd ../CMProv2016_WGS84
spatialite_tool -i -shp CMprov2016_WGS84 -d $DB -t adm2 -g geometry -c UTF-8
cd ../Reg2016_WGS84
spatialite_tool -i -shp Reg_2016_WGS84 -d $DB -t adm1 -g geometry -c UTF-8
cd ../../
rm Limiti_2016_WGS84.zip
rm -r Limiti_2016_WGS84
spatialite db.sqlite "CREATE TABLE catalog (url text PRIMARY KEY,api_url text,name text NOT NULL);"
spatialite db.sqlite "CREATE TABLE IF NOT EXISTS dataset(id text PRIMARY KEY,license_title text,maintainer text,encoding text,issued text,temporal_start text,private text,creation_date text,num_tags integer,frequency text,publisher_name text,metadata_created text,temporal_end text,metadata_modified text,author text,author_email text,theme text,site_url text,state text,version text,license_id text,type text,holder_name text,holder_identifier text,fields_description text,creator_user_id text,maintainer_email text,num_resources integer,name  text,isopen text,url text,notes text,owner_org text,modified text,publisher_identifier text,geographical_name text,license_url text,title text,revision_id text,identifier text,creator_name text,creator_identifier text,conforms_to text,language text,alternate_identifier text,is_version_of text,contact text,geographical_geonames_url text,portalUrl text);"
spatialite db.sqlite "CREATE TABLE IF NOT EXISTS resource (id text PRIMARY KEY,cache_last_updated text,package_id text,webstore_last_updated text,datastore_active text,size text,state text,hash text,description text,format text,last_modified text,url_type text,mimetype text,cache_url text,name text,created text,url text,webstore_url text,mimetype_inner text,position integer,revision_id text,resource_type text,distribution_format text);"
spatialite db.sqlite "CREATE TABLE res_in_dataset (dataset_id text, resource_id text,PRIMARY KEY (dataset_id, resource_id));"
spatialite db.sqlite "CREATE TABLE organization(id text PRIMARY KEY,description text,created text,title text,name text,is_organization text,state text,image_url text,revision_id text,type text,approval_status text);"
spatialite db.sqlite "CREATE TABLE org_in_dataset (dataset_id text, organization_id text,PRIMARY KEY (dataset_id, organization_id));"
spatialite db.sqlite "CREATE TABLE dataset_is_updated(dataset_id text PRIMARY KEY,result text);"
spatialite db.sqlite "CREATE TABLE email_verification(dataset_id text PRIMARY KEY,  maintainer_email text,author_email text,contact text);"
spatialite db.sqlite "CREATE TABLE resource_controls(resource_id text PRIMARY KEY,response_code integer,is_downloadable boolean,format_correspondence boolean,is_empty boolean,is_correct boolean,log text,correct_encoding text,declared_format text,found_format text,processed text,diretto text,geo_processed text,geo_valid text,md5sum text);"
