#include <string>
#include <vector>
#include <iostream>
#include <istream>
#include <ostream>
#include <iterator>
#include <sstream>
#include <algorithm>

using namespace std;

//can use strok but do not feel like to
vector<string> split(const char *str, char c = ' '){
    vector<string> result;
    do{
        const char *begin = str;

        while(*str != c && *str){
            str++;
        }

        string cur_str = string(begin, str);
        //in the case of consequtive delimiters, produce string will be a single '\0' char, ignore it
        if (cur_str.length() > 0){
          result.push_back(cur_str);
        }

    } while (0 != *str++);

    return result;
}

//tokenize each line of input file wrt. white spaces, print the first two tokens
int main(){

  string line;
  while (getline(cin, line)){
    vector<string> tokens = split(line.c_str());
    if (tokens.size() < 2) {
      continue;
    }
    cout << tokens[0] << tokens[1] << "," << endl;
  }
  return 0;
}

