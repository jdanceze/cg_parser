import csv
import configparser
from signature_convert import convert_signature

config = configparser.ConfigParser()
config.read('settings.ini')

coverage_file = config.get('Paths', 'coverage_file')
covids_file = config.get('Paths', 'covids_file')
refine_csv_file = config.get('Paths', 'refine_csv_file')
cov_set_file = config.get('Paths', 'cov_set_file')
labeled_csv_file = config.get('Paths', 'labeled_csv_file')

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

data = []
with open(refine_csv_file, 'r') as file:
    reader = csv.DictReader(file)
    header = reader.fieldnames
    for row in reader:
        data.append(row)

methods = []
with open(cov_set_file, 'r') as file:
    methods = [line.strip() for line in file.readlines()]


for row in data:
    if row['method'] in methods or row['target'] in methods:
        row['label'] = '1'
    else:
        row['label'] = '0'

with open(labeled_csv_file, 'w', newline='') as file:
    writer = csv.DictWriter(file, fieldnames=['method', 'offset', 'target', 'label'])
    writer.writeheader()
    writer.writerows(data)

print("Labeled data created successfully.")
