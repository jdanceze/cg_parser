import re

def convert_signature(signature):
    match = re.match(r'<(.+?):(.+)>', signature)
    if not match:
        "this is not a method signature"
        return signature
    class_name = match.group(1).strip()
    method_signature = match.group(2).strip()
    print("methodsignature: ",method_signature)
    #match = re.match(r'(\w+)\s+(\w+)\(', method_signature)
    #match = re.match(r'(.+?)\s+(\w+)\(', method_signature)
    match = re.match(r'(.+?)\s+(.+?)\(', method_signature)
    if not match:
        # if method_signature.startswith('L'):
        #     method_signature = method_signature[1:-1].replace('/', '.')
        #     class_name, method_name = signature.split('; ')
        #     class_name = class_name[1:]
        #     if method_name.startswith('access$'):
        #         method_name = method_name[7:]
        #     method_name = method_name.replace('/', '.')
        #     return f"{class_name} {method_name}"
        return signature

    return_type = match.group(1)
    method_name = match.group(2)
    #print(return_type)

    if return_type == 'V':
        new_return_type = 'void'
    elif return_type == 'Z':
        new_return_type = 'boolean'
    elif return_type == 'B':
        new_return_type = 'byte'
    elif return_type == 'C':
        new_return_type = 'char'
    elif return_type == 'S':
        new_return_type = 'short'
    elif return_type == 'I':
        new_return_type = 'int'
    elif return_type == 'J':
        new_return_type = 'long'
    elif return_type == 'F':
        new_return_type = 'float'
    elif return_type == 'D':
        new_return_type = 'double'
    else:
        #print("test")
        if return_type.startswith('L'):
            new_return_type = return_type[1:].replace('/', '.')
            new_return_type = new_return_type[:-1]
            #new_params.append(param[1:].replace('/', '.'))
        else:
            #new_params.append(param.replace('/', '.'))
            new_return_type = return_type

    params_start_index = method_signature.find('(')
    params_end_index = method_signature.find(')')
    params = method_signature[params_start_index+1:params_end_index]
    new_params = []
    for param in params.split(';'):
        param = param.strip()
        if param:
            if param == 'Z':
                new_params.append('boolean')
            elif param == 'B':
                new_params.append('byte')
            elif param == 'C':
                new_params.append('char')
            elif param == 'S':
                new_params.append('short')
            elif param == 'I':
                new_params.append('int')
            elif param == 'J':
                new_params.append('long')
            elif param == 'F':
                new_params.append('float')
            elif param == 'D':
                new_params.append('double')
            else:
                if param.startswith('L'):
                    new_params.append(param[1:].replace('/', '.'))
                else:
                    new_params.append(param.replace('/', '.'))

    new_method_signature = new_return_type + ' ' + method_name + '(' + ','.join(new_params) + ')'
    #new_method_signature = new_return_type + ' ' + "tesxcvxcvxcvcxt" + '(' + ', '.join(new_params) + ')'
    return '<' + class_name + ': ' + new_method_signature + '>'


# old_signature ='<com.androidsx.rateme.RateMeDialog$Builder: Lcom/androidsx/rateme/RateMeDialog$Builder; setBodyTextColor(I)>'
# new_signature = convert_signature(old_signature)
# print(new_signature) # <com.digitalcosmos.shimeji.mascotselector.MainFragment$2: void onClick(android.view.View)>
