//
//  main.cpp
//  CppSocketBankServer
//
//  Created by Shadowshine on 2/27/13.
//  Copyright (c) 2013 FHNW. All rights reserved.
//

#include "Server.h"

int main( int argc, char *argv[] )
{
    Server s(8080);
    s.start();
}
