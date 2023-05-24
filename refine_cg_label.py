import configparser
import csv

config = configparser.ConfigParser()
config.read('settings.ini')

labeled_csv_file = config.get('Paths', 'labeled_csv_file')
combinationWithExtraFeatures_file = config.get('Paths', 'combinationWithExtraFeatures_file')
output_file = 'output.csv'  # Specify the output file name

# Read the labels from the labeled CSV file
labels = {}
with open(labeled_csv_file, 'r') as file:
    reader = csv.reader(file)
    next(reader)  # Skip the header row
    for row in reader:
        method = row[0]
        target = row[2]
        label = row[3]
        labels[(method, target)] = label

# Assign labels to the new CSV file
with open(combinationWithExtraFeatures_file, 'r') as file:
    reader = csv.reader(file)
    header = next(reader)  # Read the header row

    with open(output_file, 'w', newline='') as output_file:
        writer = csv.writer(output_file)
        writer.writerow(header)  # Write the header row to the output file

        for row in reader:
            method = row[0]
            target = row[2]
            label = labels.get((method, target), "0")  # Get the label from the labels dictionary, defaulting to "0" if not found
            row.append(label)  # Add the label to the row
            writer.writerow(row)  # Write the updated row to the output file
