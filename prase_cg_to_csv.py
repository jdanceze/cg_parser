import csv
import re
import configparser


config = configparser.ConfigParser()
config.read('settings.ini')

soot_cg_file = config.get('Paths', 'soot_cg_file')
raw_cg_csv_file = config.get('Paths', 'raw_cg_csv_file')

with open(soot_cg_file, 'r') as file:
    callgraph_content = file.read()

pattern = r'#EDGE_FROM#([^#]+)#IN#[^#]+#TO#([^#]+)'
matches = re.findall(pattern, callgraph_content)
callgraph_data = [{'method': match[0].strip(), 'offset': "0", 'target': match[1].strip()} for match in matches]

# callgraph_data = [{key: value.replace('"', '') for key, value in entry.items()} for entry in callgraph_data]

with open(raw_cg_csv_file, 'w', newline='') as file:
    writer = csv.DictWriter(file, fieldnames=['method', 'offset','target'])
    writer.writeheader()
    writer.writerows(callgraph_data)

print("CSV file created successfully.")
