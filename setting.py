import sys
import configparser


config = configparser.ConfigParser()
config.read('settings.ini')

soot_cg_file = sys.argv[1]
covids_file = sys.argv[2]
coverage_file = sys.argv[3]

config.set('Paths', 'soot_cg_file', soot_cg_file)
config.set('Paths', 'covids_file', covids_file)
config.set('Paths', 'coverage_file', coverage_file)

with open('settings.ini', 'w') as configfile:
    config.write(configfile)

print('soot_cg_file:', soot_cg_file)
print('covids_file:', covids_file)
print('coverage_file:', coverage_file)
