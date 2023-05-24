



# Read the labels from the CSV file
labels = {}
with open(analysisfile, 'r') as file:
    reader = csv.reader(file)
    next(reader)  # Skip the header row
    for row in reader:
        method = row[0]
        target = row[2]
        label = row[3]
        labels[(method, target)] = label