import configparser
from signature_convert import convert_signature

config = configparser.ConfigParser()
config.read('settings.ini')

coverage_file = config.get('Paths', 'coverage_file')
covids_file = config.get('Paths', 'covids_file')
cov_set_file = config.get('Paths', 'cov_set_file')

with open(coverage_file, 'r') as coverage:
    lines = [line.strip() for line in coverage if not line.startswith(('recorder:', 'version:', '###'))]
    line_numbers = [int(line) for line in lines]
    line_numbers = list(dict.fromkeys(line_numbers))


with open(covids_file, 'r') as covids:
    method_signatures = [line.strip() for line in covids]

result = [method_signatures[line_number - 1] for line_number in line_numbers]

signate_set = set()
#id_set = set()

for signature in result:
    print("old: ",signature)
    new_signature = convert_signature(signature)
    print("new: ",new_signature)
    print("====================================")
    signate_set.add(new_signature)


with open(cov_set_file, 'w') as f:
    for signature in signate_set:
        f.write(signature)
        f.write("\n")


