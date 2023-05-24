import configparser
from signature_convert import convert_signature

config = configparser.ConfigParser()
config.read('settings.ini')

coverage_file = config.get('Paths', 'coverage_file')
covids_file = config.get('Paths', 'covids_file')


with open(coverage_file, 'r') as coverage:
    lines = [line.strip() for line in coverage if not line.startswith(('recorder:', 'version:', '###'))]
    line_numbers = [int(line) for line in lines]


with open(covids_file, 'r') as covids:
    method_signatures = [line.strip() for line in covids]

result = [method_signatures[line_number - 1] for line_number in line_numbers]


for signature in result:
    print("old: ",signature)
    print("new: ",convert_signature(signature))
    print("====================================")

