def replace_with_preserve(string, preserve_list):
    preserved_string = ''
    replaced_string = string  # Initialize with original string
    for preserve in preserve_list:
        if preserve in string:
            preserved_string = preserve
            replaced_string = string.replace(preserved_string, 'TEMPSTRING')
            break
    replaced_string = replaced_string.replace('_', '/')
    replaced_string = replaced_string.replace('TEMPSTRING', preserved_string)

    return replaced_string


def process_file(file_path, preserve_list):
    replaced_strings = []
    with open(file_path, 'r') as file:
        for line in file:
            string = line.strip()
            replaced_string = replace_with_preserve(string, preserve_list)
            replaced_strings.append(replaced_string)
    return replaced_strings


# Example usage
preserve_list = ["food_drink","auto_t300", "auto_t400", "beautyT100_beautyT200_beautyT300_beautyT400_bookT100_bookT200_bookT300", "bookT4_bussiT1234", "comicT1234_commuT1234_datingT1234_eduT1234", "edu_enter_event_house_lib_media", "enterT1234_eventT12", "eventT3_financeT1234_foodT1", "foodT234_houseT12", "houseT34_libT1234", "lifestyleT1234_medicalT12", "medicalT34_musicT1234", "musi_new_parent_prod_shop_soci", "newsT1234_parentT1234_photoT1234_prodT1234_shopT1", "sport_tool_tran_wea_auto1_auto2"]
file_path = './sample/dir.txt'

replaced_strings = process_file(file_path, preserve_list)

# Print the replaced strings
for string in replaced_strings:
    print(string)
