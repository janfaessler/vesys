//
//  Server.cpp
//  CppSocketBankServer
//
//  Created by Shadowshine on 2/27/13.
//  Copyright (c) 2013 FHNW. All rights reserved.
//

#include "Server.h"
#include "RequstHandler.h"
#include <boost/thread.hpp>  

typedef unsigned long ulong;

using boost::asio::ip::tcp;
using namespace std;

void Server::start() {
    
    boost::asio::io_service ioService;
    unsigned short port = 8001;
    tcp::acceptor acceptor(ioService, tcp::endpoint(tcp::v4(), port));
    
    cout << "Server startet on port " << port << endl;
    tcp::socket *socket = new tcp::socket(ioService);
    
    
    
    acceptor.accept(*socket);
    boost::thread workerThread(boost::bind(&Server::handleRequest,this,socket));
            
}

void Server::handleRequest(tcp::socket* socket) {
    cout << "Server::handleRequest" << endl;
    Request r = receiveResult(socket);
    sendCommand(socket, "recieved", r.getCommand());
    socket->close();
}

#pragma mark -
#pragma mark Connection Helper

Server::Request Server::receiveResult(tcp::socket* socket) {
    char buffer[500];
    socket->read_some(boost::asio::buffer(buffer));
    string msg = buffer;
    
    ulong split = msg.find(':',0);
    ulong end = msg.find(';', split);
    
    Request req(msg.substr( 0, split), msg.substr( split + 1, end - split - 1));
    cout << "Server->recv: " << req.getCommand() << ":" << req.getParam() << endl;
    return req;
}

void Server::sendCommand(tcp::socket* socket, string command, string param) {
    string msg = command;
    msg.append(":");
    msg.append(param);
    msg.append(";\n");
    cout << "Server->send: " << command << ":" << param << endl;
    socket->write_some(boost::asio::buffer(msg));
}