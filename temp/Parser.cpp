#include "Parser.h"
Parser::Parser(Lexer& lex_) {
    lex = &lex_;
}
vector<string> Parser::get_stmt() {
    v.clear();
    stmt();
    return v;
}
void Parser::stmt() {
    lookahead = lex->getNextLex();
    if (lookahead=="EOF")
        return;
    switch (lookahead[0]) {
        case 'C': create_stmt(); break;
        case 'I': insert_stmt(); break;
        case 'D': delete_stmt(); break;
        case 'S': query_stmt(); break;
        default: throw("Lexer error at line " + Data::toStr(lex->getLine()));
    }
}
void Parser::create_stmt() {
    match("CREATE");
    match("TABLE");
    match("ID");
    match("(");
    decl_list();
    match(")");
    match(";");
}
void Parser::insert_stmt() {
    match("INSERT");    
    match("INTO");
    match("ID");
    match("(");
    column_list();
    match(")");
    match("VALUES");
    match("(");
    value_list();
    match(")");
    match(";");
}
void Parser::delete_stmt(){
    match("DELETE");
    match("FROM");
    match("ID");
    where_clause();
    match(";");
}
void Parser::query_stmt() {
    match("SELECT");
    select_list();
    match("FROM");
    match("ID");
    where_clause();
    match(";");
}
void Parser::decl_list() {
    decl();
    if (lookahead == ",") {
        match(",");
        decl_list();
    }
}
void Parser::decl() {
    if (lookahead[2] == ':') {
        match("ID");
        match("INT");
        default_spec();
        return;
    }
    if (lookahead == "PRIMARY") {
        match("PRIMARY");
        match("KEY");
        match("(");
        column_list();
        match(")");
    }
}
void Parser::default_spec() {
    if (lookahead == "DEFAULT") {
        match("DEFAULT");
        match("=");
        match("NUM");
    }
}
void Parser::column_list() {
    match("ID");
    if (lookahead == ",") {
        match(",");
        column_list();
    }
}

void Parser::value_list() {
    match("NUM");
    if (lookahead == ",") {
        match(",");
        value_list();
    }
}

void Parser::where_clause() {
    if (lookahead == "WHERE") {
        match("WHERE");
        conjunct_list();
    }
}
void Parser::conjunct_list() {
    _bool();
    if (lookahead == "&&") {
        match("&&");
        conjunct_list();
    }
}
void Parser::_bool() {
    operand();
    rop();
    operand();
}
void Parser::operand() {
    if (lookahead[0] >= '0' && lookahead[0] <= '9') {
        match("NUM");
        return;
    }
    if (lookahead[2] == ':') {
        match("ID");
        return;
    }
}
void Parser::rop() {
    if (lookahead == "<>") {
        match("<>");
        return;
    }
    if (lookahead == "==") {
        match("==");
        return;
    }
    if (lookahead == "<") {
        match("<");
        return;
    }
    if (lookahead == ">") {
        match(">");
        return;
    }
    if (lookahead == ">=") {
        match(">=");
        return;
    }
    if (lookahead == "<=") {
        match("<=");
        return;
    }
}
void Parser::select_list() {
    if (lookahead == "*") {
        match("*");
        return;
    }
    column_list();
}

void Parser::match(string temp) {
    if (temp == "ID") {
        if (lookahead[2]==':') {
            v.push_back(lookahead);
            lookahead = lex->getNextLex();
            return;
         } else
            throw ("Parser error at line " + Data::toStr(lex->getLine()));
    }
    if (temp == "NUM") {
        if (lookahead[0] > '0' && lookahead[0] <= '9') {
            v.push_back(lookahead);
            lookahead = lex->getNextLex();
            return;
        } else
            throw ("Parser error at line " + Data::toStr(lex->getLine()));
    }
    if (lookahead == temp) {
        v.push_back(lookahead);
        if (temp != ";") 
            lookahead = lex->getNextLex();
        return;
    } else
        throw ("Parser error at line " + Data::toStr(lex->getLine()));
}
