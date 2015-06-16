#include "Traversal.h"
using namespace std;

Traversal::Traversal(DB& _db, SymbolCtrl& _sym) {
    myDB = _db;
    mySym = &_sym;
}

void Traversal::tRoot(node root) {
    try{
        if(root.val == "CREATE") {
            createTable(root);
        } else if (root.val == "INSERT") {
            insertTable(root);
        } else if (root.val == "SELECT") {
            selectTable(root);
        } else if (root.val == "DELETE") {
            deleteTable(root);
        } else {
            cout << "Error!" << endl;
        }
    } catch(string a) {
        cerr << a << endl;
    }
}

void Traversal::createTable(node root) {
    string tablename = root.subs[0].subs[0].val;
    if (myDB.addTable(tablename)) {
        try {
            addCols(root.subs[1], tablename);
            if (root.subs.size() == 3) {
                priCols(root.subs[2], tablename);
            }
        } catch (string s) {
            throw(s);
        }
        cout << mySym->getName(tablename) << " table create success!" << endl;
    } else {
        string tem = "Table Exist!";
        throw(tem);
    }
}

void Traversal::insertTable(node root) {
    string tablename = root.subs[0].subs[0].val;
    if (!myDB.tableExist(tablename)) {
        string tem = "Table don`t Exist!";
        throw(tem);
    }
    Table* table;
    table = &myDB.getTable(tablename);
    map<string, int> cm;
    if (root.subs[1].subs.size() != root.subs[2].subs.size()) {
        string tem = "Error Insert!";
        throw(tem);
    }
    for(int i = 0 ; i < root.subs[1].subs.size(); i++) {
        cm[root.subs[1].subs[i].subs[0].val] = Data::toNum(root.subs[2].subs[i].subs[0].val);
    }
    try {
        table->addRowData(cm);
        cout << "insert success!" << endl;
    } catch (string s) {
        throw(s);
    }
}

void Traversal::deleteTable(node root) {
    string tablename = root.subs[0].subs[0].val;
    if (!myDB.tableExist(tablename)) {
        string tem = "Table don`t Exist!";
        throw(tem);
    }
    Table* table;
    table = &myDB.getTable(tablename);
    vector<map<string, int>>* rows;
    rows = &table->getRows();
    vector<map<string, int>>::iterator vit;
    try {
        if (root.subs[1].subs.size() == 0) {
            rows->erase(rows->begin(), rows->end());
        } else {
            vit = rows->begin();
            while (vit != rows->end()) {
                if(conj(root.subs[1].subs[0], *vit)) {
                    vit = rows->erase(vit);
                } else {
                    vit++;
                }
            }
        }
        cout << "delete success!" << endl;
    } catch (string s) {
        throw(s);
    }
}

void Traversal::selectTable(node root) {
    string tablename = root.subs[1].subs[0].val;
    if (!myDB.tableExist(tablename)) {
        string tem = "Table don`t Exist!";
        throw(tem);
    }
    Table* table;
    table = &myDB.getTable(tablename);
    vector<map<string, int>> rows = table->getRows();
    vector<string> colname = getColName(root.subs[0]);
    vector<string> colnames = table->getColNames();
    map<string, int> testrow;
    for (int i = 0; i < colnames.size(); i++) {
        testrow[colnames[i]] = 0;
    }
    if (colname[0] != "*") {
        for (int i = 0; i < colname.size(); i++) {
            int j = 0;
            for (;j < colnames.size(); j++) {
                if (colname[i] == colnames[j]) {
                    break;
                }
            }
            if (j == colnames.size()) {
                string tem = "Some Column don`t Exist!";
                throw(tem);
            }
        }
    }
    try {
        if (root.subs[2].subs.size() == 0) {
            if (colname[0] == "*") {
                changeline(colnames.size());
                for (int i = 0; i < colnames.size(); i++) {
                    int a, b;
                    a = (10 - mySym->getName(colnames[i]).length()) / 2;
                    b = 10 - mySym->getName(colnames[i]).length() - a;
                    cout << "|";
                    space(a);
                    cout << mySym->getName(colnames[i]);
                    space(b);
                }
                cout << "|\n";
                changeline(colnames.size());
                for (int i = 0; i < rows.size(); i++) {
                    for (int j = 0; j < colnames.size(); j++) {
                        int a, b;
                        a = (10 - lenfornum(rows[i][colnames[j]])) / 2;
                        b = 10 - lenfornum(rows[i][colnames[j]]) - a;
                        cout << "|";
                        space(a);
                        cout << rows[i][colnames[j]];
                        space(b);
                    }
                    cout << "|\n";
                    changeline(colnames.size());
                }
            } else {
                changeline(colname.size());
                for (int i = 0; i < colname.size(); i++) {
                    int a, b;
                    a = (10 - mySym->getName(colname[i]).length()) / 2;
                    b = 10 - mySym->getName(colname[i]).length() - a;
                    cout << "|";
                    space(a);
                    cout << mySym->getName(colname[i]);
                    space(b);
                }
                cout << "|\n";
                changeline(colname.size());
                for (int i = 0; i < rows.size(); i++) {
                    for (int j = 0; j < colname.size(); j++) {
                        int a, b;
                        a = (10 - lenfornum(rows[i][colname[j]])) / 2;
                        b = 10 - lenfornum(rows[i][colname[j]]) - a;
                        cout << "|";
                        space(a);
                        cout << rows[i][colname[j]];
                        space(b);
                    }
                    cout << "|\n";
                    changeline(colname.size());
                }
            }
        } else {
            conj(root.subs[2].subs[0], testrow);
            if (colname[0] == "*") {
                changeline(colnames.size());
                for (int i = 0; i < colnames.size(); i++) {
                    int a, b;
                    a = (10 - mySym->getName(colnames[i]).length()) / 2;
                    b = 10 - mySym->getName(colnames[i]).length() - a;
                    cout << "|";
                    space(a);
                    cout << mySym->getName(colnames[i]);
                    space(b);
                }
                cout << "|\n";
                changeline(colnames.size());
                for (int i = 0; i < rows.size(); i++) {
                    if (conj(root.subs[2].subs[0], rows[i])) {
                        for (int j = 0; j < colnames.size(); j++) {
                            int a, b;
                            a = (10 - lenfornum(rows[i][colnames[j]])) / 2;
                            b = 10 - lenfornum(rows[i][colnames[j]]) - a;
                            cout << "|";
                            space(a);
                            cout << rows[i][colnames[j]];
                            space(b);
                        }
                        cout << "|\n";
                        changeline(colnames.size());
                    }
                }
            } else {
                changeline(colname.size());
                for (int i = 0; i < colname.size(); i++) {
                    int a, b;
                    a = (10 - mySym->getName(colname[i]).length()) / 2;
                    b = 10 - mySym->getName(colname[i]).length() - a;
                    cout << "|";
                    space(a);
                    cout << mySym->getName(colname[i]);
                    space(b);
                }
                cout << "|\n";
                changeline(colname.size()); 
                for (int i = 0; i < rows.size(); i++) {
                    if (conj(root.subs[2].subs[0], rows[i])) {
                        for (int j = 0; j < colname.size(); j++) {
                            int a, b;
                            a = (10 - lenfornum(rows[i][colname[j]])) / 2;
                            b = 10 - lenfornum(rows[i][colname[j]]) - a;
                            cout << "|";
                            space(a);
                            cout << rows[i][colname[j]];
                            space(b);
                        }
                        cout << "|\n";
                        changeline(colname.size());
                    }
                }
            }
        }
    } catch (string s) {
        throw(s);
    }
}

void Traversal::addCols(node root, string tableName) {
    Table* table;
    table = &myDB.getTable(tableName);
    vector<string> coln = getColName(root);
    map<string, int> colv = getColValue(root);
    map<string, int>::iterator it;
    for (int i = 0; i < coln.size(); i++) {
        it = colv.find(coln[i]);
        if (it != colv.end()) {
            if (table->colExist(coln[i])) {
                string tem = "Column Exist!";
		        throw(tem);
            }
            table->addCol(coln[i], false, true, it->second);
        } else {
            if (table->colExist(coln[i]))    {
                string tem = "Column Exist!";
		        throw(tem);
            }
            table->addCol(coln[i], false, false, 0);
        }
    }
}

void Traversal::priCols(node root, string tableName) {
    Table* table;
    table = &myDB.getTable(tableName);
    if (root.subs.size() > 0 && root.subs[0].subs.size() > 1) {
        string tem = "More than one Primary Key!";
        throw(tem);
    } else if (root.subs.size() > 0 && root.subs[0].subs.size() > 0 && root.subs[0].subs[0].subs.size() > 0) {
        if (!table->colExist(root.subs[0].subs[0].subs[0].val)) {
            string tem = "Primary Key don`t Exist!";
            throw(tem);
        }
        table->setpri(root.subs[0].subs[0].subs[0].val);
    }
}

vector<string> Traversal::getColName(node root) {
    vector<string> colname;
    if (root.subs[0].val == "*") {
        colname.push_back("*");
        return colname;
    }
    for(int i = 0; i < root.subs.size(); i++) {
        colname.push_back(root.subs[i].subs[0].val);
    }
    return colname;
}

map<string,int> Traversal::getColValue(node root) {
    map<string, int> ms;
    for(int i = 0; i < root.subs.size(); i++) {
        if(root.subs[i].subs[0].subs.size() > 0) {
            int num = Data::toNum(root.subs[i].subs[0].subs[0].subs[0].val);
            ms[root.subs[i].subs[0].val] = num;
        }
    }
    return ms;
}

bool Traversal::conj(node root, map<string, int> col) {
    if (root.subs[0].val == "&&" ) {
        return conj(root.subs[0].subs[0], col) && conj(root.subs[0].subs[1], col);
    } else if (root.subs[0].val == "||") {
        return conj(root.subs[0].subs[0], col) || conj(root.subs[0].subs[1], col);
    } else if (root.subs[0].val == "!") {
        return !(conj(root.subs[0].subs[0], col));
    } else if (root.subs[0].val == "<>") {
        int num1, num2;
        if (root.subs[0].subs[0].val[0] == 'i') {
            if(col.find(root.subs[0].subs[0].val) == col.end()) {
                string tem = "The Column don`t Exist!";
                throw(tem);
            }
            num1 = col[root.subs[0].subs[0].val];
            num2 = Data::toNum(root.subs[0].subs[1].val);
        } else {
            if(col.find(root.subs[0].subs[1].val) == col.end()) {
                string tem = "The Column don`t Exist!";
                throw(tem);
            }
            num1 = Data::toNum(root.subs[0].subs[0].val);
            num2 = col[root.subs[0].subs[1].val];
        }
        return (num1 != num2);
    } else if (root.subs[0].val == ">") {
        int num1, num2;
       if (root.subs[0].subs[0].val[0] == 'i') {
            if(col.find(root.subs[0].subs[0].val) == col.end()) {
                string tem = "The Column don`t Exist!";
                throw(tem);
            }
            num1 = col[root.subs[0].subs[0].val];
            num2 = Data::toNum(root.subs[0].subs[1].val);
        } else {
            if(col.find(root.subs[0].subs[1].val) == col.end()) {
                string tem = "The Column don`t Exist!";
                throw(tem);
            }
            num1 = Data::toNum(root.subs[0].subs[0].val);
            num2 = col[root.subs[0].subs[1].val];
        }
        return (num1 > num2);
    } else if (root.subs[0].val == ">=") {
        int num1, num2;
        if (root.subs[0].subs[0].val[0] == 'i') {
            if(col.find(root.subs[0].subs[0].val) == col.end()) {
                string tem = "The Column don`t Exist!";
                throw(tem);
            }
            num1 = col[root.subs[0].subs[0].val];
            num2 = Data::toNum(root.subs[0].subs[1].val);
        } else {
            if(col.find(root.subs[0].subs[1].val) == col.end()) {
                string tem = "The Column don`t Exist!";
                throw(tem);
            }
            num1 = Data::toNum(root.subs[0].subs[0].val);
            num2 = col[root.subs[0].subs[1].val];
        }
        return (num1 >= num2);
    } else if (root.subs[0].val == "<") {
        int num1, num2;
        if (root.subs[0].subs[0].val[0] == 'i') {
            if(col.find(root.subs[0].subs[0].val) == col.end()) {
                string tem = "The Column don`t Exist!";
                throw(tem);
            }
            num1 = col[root.subs[0].subs[0].val];
            num2 = Data::toNum(root.subs[0].subs[1].val);
        } else {
            if(col.find(root.subs[0].subs[1].val) == col.end()) {
                string tem = "The Column don`t Exist!";
                throw(tem);
            }
            num1 = Data::toNum(root.subs[0].subs[0].val);
            num2 = col[root.subs[0].subs[1].val];
        }
        return (num1 < num2);
    } else if (root.subs[0].val == "<=") {
        int num1, num2;
        if (root.subs[0].subs[0].val[0] == 'i') {
            if(col.find(root.subs[0].subs[0].val) == col.end()) {
                string tem = "The Column don`t Exist!";
                throw(tem);
            }
            num1 = col[root.subs[0].subs[0].val];
            num2 = Data::toNum(root.subs[0].subs[1].val);
        } else {
            if(col.find(root.subs[0].subs[1].val) == col.end()) {
                string tem = "The Column don`t Exist!";
                throw(tem);
            }
            num1 = Data::toNum(root.subs[0].subs[0].val);
            num2 = col[root.subs[0].subs[1].val];
        }
        return (num1 <= num2);
    } else if (root.subs[0].val == "==") {
        int num1, num2;
        if (root.subs[0].subs[0].val[0] == 'i') {
            if(col.find(root.subs[0].subs[0].val) == col.end()) {
                string tem = "The Column don`t Exist!";
                throw(tem);
            }
            num1 = col[root.subs[0].subs[0].val];
            num2 = Data::toNum(root.subs[0].subs[1].val);
        } else {
            if(col.find(root.subs[0].subs[1].val) == col.end()) {
                string tem = "The Column don`t Exist!";
                throw(tem);
            }
            num1 = Data::toNum(root.subs[0].subs[0].val);
            num2 = col[root.subs[0].subs[1].val];
        }
        return (num1 == num2);
    }
}

void Traversal::changeline(int n) {
    for (int i = 0; i < n; i++) {
        cout << "|----------";
    }
    cout << "|\n";
}
void Traversal::space(int n) {
    if (n < 0)  {
        n = 1;
    }
    for (int i = 0; i < n; i++) {
        cout << " ";
    }
}
int Traversal::lenfornum(int n) {
    int tem;
    if (n >= 0) {
        tem = 1;
    } else {
        n *= -1;
        tem = 2;
    }
    while (n >= 10) {
        tem++;
        n /= 10;
    }
    return tem;
}
