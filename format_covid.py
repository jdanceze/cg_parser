import configparser
from signature_convert import convert_signature

config = configparser.ConfigParser()
config.read('settings.ini')

covids_file = config.get('Paths', 'covids_file')
dummy_main_method_file = config.get('Paths', 'dummy_main_method_file')
processed_method_file = config.get('Paths', 'processed_method_file')
method_reference_file = config.get('Paths', 'method_reference_file')

with open(covids_file, 'r') as covids:
    method_signatures = [line.strip() for line in covids]

for signature in method_signatures:
    # print("old: ",signature)
    # print("new: ",convert_signature(signature))
    # print("====================================")

    with open(processed_method_file, 'a') as f:
        f.write(convert_signature(signature))
        f.write("\n")


with open(method_reference_file, 'w') as outfile:
    for fname in [dummy_main_method_file, processed_method_file]:
        with open(fname) as infile:
            outfile.write(infile.read())

