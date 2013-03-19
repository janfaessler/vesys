//
//  Bank.h
//  CppSocketBankServer
//
//  Created by Shadowshine on 2/27/13.
//  Copyright (c) 2013 FHNW. All rights reserved.
//

#ifndef __CppSocketBankServer__Bank__
#define __CppSocketBankServer__Bank__

#include <iostream>
#include <map>
#include <set>
#include <string>
#include "Account.h"

using namespace std;

class Bank {
    map<string, Account> *accounts;
    
public:
    Bank() {
        accounts = new map<string,Account>();
    }
    
    string createAccount(string owner);
    bool closeAccount(string number);
    Account* getAccount(string number);
    set<string> getAccountNumbers();

    void transfer(Account* a, Account* b, double amount);
};

#endif /* defined(__CppSocketBankServer__Bank__) */
