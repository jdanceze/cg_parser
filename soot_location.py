import os
import sys
def read_mapping_file(mapping_file):
    mapping = {}
    with open(mapping_file, 'r') as file:
        for line in file:
            apk_location, name = line.strip().split(',')
            mapping[name] = apk_location
    return mapping

def lookup_apk_location(mapping, name):
    return mapping.get(name, None)

def get_apk_name(apk_location):
    return apk_location.split('/')[-1]

def create_directory(apk_name):
    directory_name = apk_name.split('.apk')[0]  # Remove the ".apk" extension
    return directory_name

def process_data(data_file, mapping_file):
    mapping = read_mapping_file(mapping_file)
    
    with open(data_file, 'r') as file:
        for line in file:
            parts = line.strip().split(',')
            name = parts[0]
            apk_location = lookup_apk_location(mapping, name)
            if apk_location:
                apk_name = get_apk_name(apk_location)
                directory_name = create_directory(apk_name)
                apk_directory = '/'.join(apk_location.split('/')[:3])
                third_directory = apk_location.split('/')[3]
                #print(f"APK Location: {apk_location}")
                #print(f"APK Directory: {apk_directory}")
                #print(f"Third Directory: {third_directory}")
                print(f"{apk_directory}/output/{third_directory}")
            else:
                print(f"Name: {name} | APK Location not found")

# Define the paths to the data file and mapping file
data_file_path = sys.argv[1]
mapping_file_path = './ella_name_map.txt'

# Process the data
process_data(data_file_path, mapping_file_path)
