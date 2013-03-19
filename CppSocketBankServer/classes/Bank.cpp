//
//  Bank.cpp
//  CppSocketBankServer
//
//  Created by Shadowshine on 2/27/13.
//  Copyright (c) 2013 FHNW. All rights reserved.
//

#include "Bank.h"

using namespace std;


string Bank::createAccount(string owner) {
    string number = to_string(accounts->size());
    Account acc(owner, number);
    accounts->insert(pair<string,Account>(number,acc));
    return number;
}

bool Bank::closeAccount(string number) {
    map<string,Account>::iterator it = accounts->find(number);
    if (it != accounts->end()) {
        it->second.setInactive();
        return true;
    } else {
        return false;
    }
}

Account* Bank::getAccount(string number) {
    map<string,Account>::iterator it = accounts->find(number);
    if (it != accounts->end()) {
        return &it->second;
    } else return nullptr;
}

set<string> Bank::getAccountNumbers() {
    set<string> result;
    map<string,Account>::iterator it = accounts->begin();
    while (it != accounts->end()) {
        if (it->second.isActive()) result.insert(it->first);
        it++;
    }
    return result;
}

void Bank::transfer(Account* a, Account* b, double amount) {
    a->withdraw(amount);
    b->deposit(amount);
}