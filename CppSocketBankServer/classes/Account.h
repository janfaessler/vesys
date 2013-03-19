//
//  Account.h
//  CppSocketBankServer
//
//  Created by Shadowshine on 2/27/13.
//  Copyright (c) 2013 FHNW. All rights reserved.
//

#ifndef __CppSocketBankServer__Account__
#define __CppSocketBankServer__Account__

#include <iostream>
#include <string>

using namespace std;

class Account {
    string owner;
    string number;
    double balance;
    bool active;
    
public:
    
    class inactiveException : exception {};
    class illegalArgumentException : exception {};
    class overdrawException : exception {};
    
    Account (string o, string n) {
        owner = o;
        number = n;
    }
    
    string getNumber() { return number; }
    void setNumber(string n) { number = n; }
    
    bool isActive() { return active; }
    void setInactive() { active = false; }

    double getBalance() { return balance; }

    void deposit(double amount);
    void withdraw(double amount);
};

#endif /* defined(__CppSocketBankServer__Account__) */
