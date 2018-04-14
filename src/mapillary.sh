export MAPILLARY_EMAIL="hayashi.yuu@gmail.com"
export MAPILLARY_PASSWORD="yuu8844"
export MAPILLARY_USERNAME="hayashi"
export MAPILLARY_PERMISSION_HASH="eyJleHBpcmF0aW9uIjoiMjAyMC0wMS0wMVQwMDowMDowMFoiLCJjb25kaXRpb25zIjpbeyJidWNrZXQiOiJtYXBpbGxhcnkudXBsb2Fkcy5tYW51YWwuaW1hZ2VzIn0sWyJzdGFydHMtd2l0aCIsIiRrZXkiLCJoYXlhc2hpLyJdLHsiYWNsIjoicHJpdmF0ZSJ9LFsic3RhcnRzLXdpdGgiLCIkQ29udGVudC1UeXBlIiwiIl0sWyJjb250ZW50LWxlbmd0aC1yYW5nZSIsMCw1MDAwMDAwMF1dfQ=="
export MAPILLARY_SIGNATURE_HASH="SwRGN9K/FN+FpXPSO09LvuhBAGI="


# python /source/mapillary_tools/python/geotag_from_gpx.py /mnt/osm/img/m/ /mnt/osm/img/2016-04-09_150103.gpx

python /source/mapillary_tools/python/remove_duplicates.py /mnt/osm/img/m/ /mnt/osm/img/duplicate/

python /source/mapillary_tools/python/upload_with_preprocessing.py /mnt/osm/img/m/
