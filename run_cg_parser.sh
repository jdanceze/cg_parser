#!/bin/bash
#python3 setting.py ./com.autostart.apk/com.autostart.txt ./com.autostart.apk/_workspace_finance_10_com.autostart.apk/covids ./com.autostart.apk/_workspace_finance_10_com.autostart.apk/coverage.dat.2023-05-08-13-53-00
python3 prase_cg_to_csv.py
python3 refine_csv.py
python3 format_covid.py
python3 gen_coverage_sig_set.py
python3 label_csv.py
python3 remove_std_lib_edges.py
python3 generate_extra_information_from_dataset.py ./output/ True