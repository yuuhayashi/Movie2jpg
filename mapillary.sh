export MAPILLARY_EMAIL="dummy@mail.com"
export MAPILLARY_PASSWORD="password"
export MAPILLARY_USERNAME="dummy"
export MAPILLARY_PERMISSION_HASH="dummy"
export MAPILLARY_SIGNATURE_HASH="dummy"

python /source/mapillary_tools/python/remove_duplicates.py /mnt/mapi/img/m/ /mnt/mapi/img/duplicate/
python /source/mapillary_tools/python/upload_with_preprocessing.py /mnt/mapi/img/m/
