import csv
import tempfile
import shutil

def rename_csv_header(filename, mapping):
    temp_filename = tempfile.NamedTemporaryFile(delete=False).name
    
    with open(filename, 'r', newline='') as file, open(temp_filename, 'w', newline='') as temp_file:
        reader = csv.reader(file)
        writer = csv.writer(temp_file)
        
        # Rename and write the new header
        header = next(reader)
        renamed_header = [mapping.get(column, column) for column in header]
        writer.writerow(renamed_header)
        
        # Write the remaining rows
        writer.writerows(reader)
    
    # Replace the original file with the modified file
    shutil.move(temp_filename, filename)

# Example usage:
mapping = {
    'label': 'wiretap',
    'wala-direct': 'wala-cge-0cfa-noreflect-intf-direct',
    'wala': 'wala-cge-0cfa-noreflect-intf-trans',
    'wala-direct#depth_from_main': 'wala-cge-0cfa-noreflect-intf-direct#depth_from_main',
    'wala-direct#src_node_in_deg': 'wala-cge-0cfa-noreflect-intf-direct#src_node_in_deg',
    'wala-direct#dest_node_out_deg': 'wala-cge-0cfa-noreflect-intf-direct#dest_node_out_deg',
    'wala-direct#dest_node_in_deg': 'wala-cge-0cfa-noreflect-intf-direct#dest_node_in_deg',
    'wala-direct#src_node_out_deg': 'wala-cge-0cfa-noreflect-intf-direct#src_node_out_deg',
    'wala-direct#repeated_edges': 'wala-cge-0cfa-noreflect-intf-direct#repeated_edges',
    'wala-direct#node_disjoint_paths_from_main': 'wala-cge-0cfa-noreflect-intf-direct#node_disjoint_paths_from_main',
    'wala-direct#edge_disjoint_paths_from_main': 'wala-cge-0cfa-noreflect-intf-direct#edge_disjoint_paths_from_main',
    'wala-direct#min_fanout': 'wala-cge-0cfa-noreflect-intf-direct#fanout',
    'wala-direct#graph_node_count': 'wala-cge-0cfa-noreflect-intf-direct#graph_node_count',
    'wala-direct#graph_edge_count': 'wala-cge-0cfa-noreflect-intf-direct#graph_edge_count',
    'wala-direct#graph_avg_deg': 'wala-cge-0cfa-noreflect-intf-direct#graph_avg_deg',
    'wala-direct#graph_avg_edge_fanout': 'wala-cge-0cfa-noreflect-intf-direct#graph_avg_edge_fanout',
    'wala#depth_from_main': 'wala-cge-0cfa-noreflect-intf-trans#depth_from_main',
    'wala#src_node_in_deg': 'wala-cge-0cfa-noreflect-intf-trans#src_node_in_deg',
    'wala#dest_node_out_deg': 'wala-cge-0cfa-noreflect-intf-trans#dest_node_out_deg',
    'wala#dest_node_in_deg': 'wala-cge-0cfa-noreflect-intf-trans#dest_node_in_deg',
    'wala#src_node_out_deg': 'wala-cge-0cfa-noreflect-intf-trans#src_node_out_deg',
    'wala#repeated_edges': 'wala-cge-0cfa-noreflect-intf-trans#repeated_edges',
    'wala#node_disjoint_paths_from_main': 'wala-cge-0cfa-noreflect-intf-trans#node_disjoint_paths_from_main',
    'wala#edge_disjoint_paths_from_main': 'wala-cge-0cfa-noreflect-intf-trans#edge_disjoint_paths_from_main',
    'wala#avg_fanout': 'wala-cge-0cfa-noreflect-intf-trans#fanout',
    'wala#graph_node_count': 'wala-cge-0cfa-noreflect-intf-trans#graph_node_count',
    'wala#graph_edge_count': 'wala-cge-0cfa-noreflect-intf-trans#graph_edge_count',
    'wala#graph_avg_deg': 'wala-cge-0cfa-noreflect-intf-trans#graph_avg_deg',
    'wala#graph_avg_edge_fanout': 'wala-cge-0cfa-noreflect-intf-trans#graph_avg_edge_fanout',
    'method': 'method',
    'offset': 'offset',
    'target': 'target'
}
filename = './output/final/combinationWithExtraFeatures_label.csv'
rename_csv_header(filename, mapping)
rename_csv_header('./output/final/combinationWithExtraFeatures.csv', mapping)
