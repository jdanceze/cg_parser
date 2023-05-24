import csv
import configparser

config = configparser.ConfigParser()
config.read('settings.ini')

soot_cg_file = config.get('Paths', 'soot_cg_file')
refine_csv_file = config.get('Paths', 'refine_csv_file')
dummy_main_method_file = config.get('Paths', 'dummy_main_method_file')

new_rows = []
dummy_main_method = []

def process_data(data):
    start_index = data.find('<')
    end_index = data.rfind('>')
    if start_index != -1 and end_index != -1:
        return data[start_index:end_index+1]
    return data

with open(soot_cg_file, 'r') as file:
    reader = csv.reader(file)
    header = next(reader)
    rows = list(reader)

#if 'method' in header and any('dummyMainMethod' in row for row in rows):
for row in rows:
    print(row[0])
    if 'dummyMainMethod' in row[0]:
        try:
            new_row = ['<dummyMainMethod>', '0', row[2]]
            new_rows.append(new_row)
            dummy_main_method.append(row[2])
        except:
            print("Error: ", row)

processed_data = [[process_data(row[0]).replace('"', ''), process_data(row[1]).replace('"', ''), process_data(row[2]).replace('"', '')] for row in rows]

with open(refine_csv_file, 'w', newline='') as file:
    writer = csv.writer(file)
    writer.writerow(header)
    for new_row in new_rows:
        writer.writerow(new_row)
    writer.writerows(processed_data)

print("CSV file created successfully.")

with open(dummy_main_method_file, 'w') as f:
    for method in dummy_main_method:
        f.write(method)
        f.write("\n")
