//
//  Account.cpp
//  CppSocketBankServer
//
//  Created by Shadowshine on 2/27/13.
//  Copyright (c) 2013 FHNW. All rights reserved.
//

#include "Account.h"

void Account::deposit(double amount) {
    if (!active) throw new inactiveException;
    if (amount < 0.0) throw new illegalArgumentException;
    balance += amount;
}

void Account::withdraw(double amount) {
    if (balance - amount < 0.0) throw new overdrawException;
    if (!active) throw new inactiveException;
    if (amount < 0.0) throw new illegalArgumentException;
    balance -= amount;
}