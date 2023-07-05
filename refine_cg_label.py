import configparser
import csv

config = configparser.ConfigParser()
config.read('settings.ini')

labeled_csv_file = config.get('Paths', 'labeled_csv_file')
combinationWithExtraFeatures_file = config.get('Paths', 'combinationWithExtraFeatures_file')
combinationWithExtraFeatures_label_file = config.get('Paths', 'combinationWithExtraFeatures_label_file')



labels = {}
with open(labeled_csv_file, 'r') as file:
    reader = csv.reader(file)
    next(reader) 
    for row in reader:
        method = row[0]
        target = row[2]
        label = row[3]
        labels[(method, target)] = label

with open(combinationWithExtraFeatures_file, 'r') as file:
    reader = csv.reader(file)
    header = next(reader)  # Read the header row
    header.append('label') 

    with open(combinationWithExtraFeatures_label_file, 'w', newline='') as output_file:
        writer = csv.writer(output_file)
        writer.writerow(header)  # Write the header row to the output file

        for row in reader:
            method = row[0]
            target = row[2]
            label = labels.get((method, target), "0")
            row.append(label)
            writer.writerow(row)
