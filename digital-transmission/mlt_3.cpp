#include <bits/stdc++.h>
using namespace std;

int main()
{
  int level;
  bool isUp = true;
  char output[3] = {'-', '0', '+'};
  string input;

  cin >> input;

  // At the beginning
  if (input[0] == '0')
  {
    cout << '0';
    level = 0;
  }
  else if (input[0] == '1')
  {
    cout << '+';
    level = 1;
  }

  // On running
  for (int i = 1; i < input.length(); i++)
  {
    if (input[i] == '0')
    {
      cout << output[level + 1];
    }
    else if (input[i] == '1')
    {
      if (level != 0)
      {
        isUp = !isUp;
        cout << output[1];
        level = 0;
      }
      else
      {
        if (isUp)
        {
          cout << output[2];
          level = 1;
        }
        else
        {
          cout << output[0];
          level = -1;
        }
      }
    }
  }

  return 0;
}