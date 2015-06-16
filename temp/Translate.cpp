#include "Translate.h"
#include <iostream>
using namespace std;

node Translate::getRoot(vector<string> v) {
	node re;
	location = 1;
	switch (v[0][0]) {
        case 'C': return create_node(v);
        case 'I': return insert_node(v);
        case 'D': return delete_node(v);
        case 'S': return select_node(v);
        default : throw("Translate Error");
    }
    return re;
}

node Translate::create_node(vector<string> v) {
	node root;
	root.val = "CREATE";
	root.subs.push_back(set_node(v));
	root.subs.push_back(cols_node(v)); 
	root.subs.push_back(pris_node(v));
	return root;
}

node Translate::insert_node(vector<string> v) {
	node root;
	root.val = "INSERT";
	root.subs.push_back(get_node(v));
	root.subs.push_back(cols_node(v)); 
	root.subs.push_back(vals_node(v));
	return root;
}

node Translate::delete_node(vector<string> v) {
    node root;
    root.val = "DELETE";
    root.subs.push_back(get_node(v));
    root.subs.push_back(where_node(v));
    return root;
}

node Translate::select_node(vector<string> v) {
    node root;
    root.val = "SELECT";
    root.subs.push_back(cols_node(v));
    root.subs.push_back(get_node(v));
    root.subs.push_back(where_node(v));
    return root;
}

node Translate::set_node(vector<string> v) {
	node root;
	root.val = "SET";
	for (; location < v.size(); location++) {
		if (v[location][0] == 'i' && v[location][1] == 'd' && v[location][2] == ':') {
			root.subs.push_back(id_node(v));		
			location++;
			break;
		}
	}
	return root;
}

node Translate::cols_node(vector<string> v) {
	node root;
	root.val = "COLS";
    if (v[1] == "*") {
        node tem;
        tem.val = "*";
        location++;
        root.subs.push_back(tem);
        return root;
    }
	for (; location < v.size(); location++) {
		if (v[location][0] == 'i' && v[location][1] == 'd' && v[location][2] == ':') {
			root.subs.push_back(col_node(v));
		}
		else if (v[location] == "PRIMARY") {
			location--;
			break;
		}
		else if (v[location] == "VALUES") {
			location--;
			break;
		}
		else if (v[location] == "FROM" ) {
		    break;
		}
	}
	return root;
}

node Translate::pris_node(vector<string> v) {
	node root;
	root.val = "PRIS";
	for (; location < v.size(); location++) {
		if(v[location] == "PRIMARY") {
			location++;
			root.subs.push_back(cols_node(v));
			break;
		}
	}
	return root;
}

node Translate::get_node(vector<string> v) {
	node root;
	root.val = "GET";
	for (; location < v.size(); location++) {
		if (v[location][0] == 'i' && v[location][1] == 'd' && v[location][2] == ':') {
		    root.subs.push_back(id_node(v));
			location++;
		    break;
		}
	}
	return root;
}

node Translate::vals_node(vector<string> v) {
	node root;
	root.val = "VALS";
	for (; location < v.size(); location++) {
		if (v[location][0] >= '0' && v[location][0] <= '9') {
			root.subs.push_back(val_node(v));
		}
	}
	return root;
}

node Translate::col_node(vector<string> v) {
	node root;
	root.val = "COL";
	root.subs.push_back(id_node(v));
	return root;
}

node Translate::val_node(vector<string> v) {
	node root, temp;
	root.val = "VAL";
	temp.val = v[location];
	root.subs.push_back(temp);
	return root;
}

node Translate::id_node(vector<string> v) {
	node root;
	root.val = v[location];
	if (location+2 < v.size() && v[location+2] == "DEFAULT") {
		location += 2;
		root.subs.push_back(default_node(v));
	}
	return root;
}

node Translate::default_node(vector<string> v) {
	node root;
	root.val = "DEFAULT";
	location += 2;
	root.subs.push_back(val_node(v));
	return root;
}

node Translate::where_node(vector<string> v) {
    node root;
    root.val = "WHERE";
    location++;
    if (location >= v.size()) {
        return root;
    }
    node ptr;
    while (location < v.size()) {
        if (v[location] == "&&" || v[location] == "||") {
            node tem, tem0;
            tem.val = "CONJ";
            tem0.val = v[location];
            location++;
            tem0.subs.push_back(conj_node(v));
            tem0.subs.push_back(ptr);
            tem.subs.push_back(tem0);
            ptr = tem;
            if (v[location+1] == "!") {
                location += 5;
            } else {
                location += 4;
            }
        } else if (v[location] == ";") {
            break;
        } else {
            ptr = conj_node(v);
            location += 3;
        }
    }
    root.subs.push_back(ptr);
    return root;
}

node Translate::conj_node(vector<string> v) {
    node root;
    node *ptr;
    ptr = &root;
    root.val = "CONJ";
    if (v[location] == "!") {
        node tem, temp;
        tem.val = "!";
        temp.val = "CONJ";
        ptr = &temp;
        tem.subs.push_back(temp);
        root.subs.push_back(tem);
        location++;
    }
    node tem0, tem1, tem2;
    tem0.val = v[location+1];
    tem1.val = v[location];
    tem2.val = v[location+2];
    tem0.subs.push_back(tem1);
    tem0.subs.push_back(tem2);
    (*ptr).subs.push_back(tem0);
    return root;
}
