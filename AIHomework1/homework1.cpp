#include <iostream>
#include <queue>

using namespace std;

#define N 7

struct Position
{
    int x;
    int y;

};

struct QueueNode
{
    Position pos;
    int dist;
};

int rowMove[] = { -1, 0, 0, 1 };
int colMove[] = { 0, -1, 1, 0 };

vector<QueueNode> v;

bool isValid(int row, int col)
{
    return (row >= 0) && (row < N) && (col >= 0) && (col < N);
}

bool validMove(int x1, int y1, int x2, int y2)
{
    if ((abs(x2 - x1) == 1 && y1 == y2) || (abs(y2 - y1) == 1 && x1 == x2))
        return true;
    else
        return false;
}

void markPath(int x, int y, char m[N][N], int dist)
{
    m[x][y] = '*';

    QueueNode node;
    for (size_t i = 0; i < v.size(); i++) {
        node = v.at(i);
        if (node.dist == dist - 1 && validMove(x, y, node.pos.x, node.pos.y)) {
            markPath(node.pos.x, node.pos.y, m, dist - 1);
            return;
        }
    }
}

bool reachTarget(char mat[N][N], Position start, Position target)
{
    if (mat[start.x][start.y] != '1' || mat[target.x][target.y] != '1') {
        cout << "Starting point or target point is unpassable..." << endl;
        return false;
    }
    bool visited[N][N];
    memset(visited, false, sizeof visited);

    visited[start.x][start.y] = true;
    queue<QueueNode> q;

    QueueNode s = { start, 0 };
    q.push(s);
    v.push_back(s);

    while (!q.empty()) {
        QueueNode currentNode = q.front();
        Position currentPosition = currentNode.pos;

        if (currentPosition.x == target.x && currentPosition.y == target.y) {
            markPath(currentPosition.x, currentPosition.y, mat, currentNode.dist);
            return true;
        }

        q.pop();

        for (int i = 0; i < 4; i++) {
            int row = currentPosition.x + rowMove[i];
            int col = currentPosition.y + colMove[i];

            if (isValid(row, col) && mat[row][col] == '1' && !visited[row][col]) {
                visited[row][col] = true;
                QueueNode adjacent = { { row, col }, currentNode.dist + 1 };
                q.push(adjacent);
                v.push_back(adjacent);
            }
        }
    }
    return false;
}

void display(char m[N][N])
{
    cout << endl;
    for (size_t i = 0; i < N; i++)
    {
        for (size_t j = 0; j < N; j++)
        {
            cout << m[i][j] << " ";
        }
        cout << endl;
    }
    cout << endl;
}

int main(){
    char mat[N][N] = {
        { '1','1','1','1','1','1','1' },
        { '1','1','1','1','1','1','1' },
        { '1','1','1','1','1','1','1' },
        { '1','1','1','1','1','1','1' },
        { '1','1','1','1','1','1','1' },
        { '1','1','1','1','1','1','1' },
        { '1','1','1','1','1','1','1' }};


    int startX, startY, targetX, targetY, numberOfZeroes;
    cout << "Setting up matrix. Enter valid coordinates (values in [0,6])..." << endl;
    cout << "Enter the coordinates of the starting point: ";
    cin >> startX;
    cin >> startY;
    cout << "Enter the coordinates of the target point: ";
    cin >> targetX;
    cin >> targetY;
    cout << "Enter the desired number of blocked positions: ";
    cin >> numberOfZeroes;

    Position start = {startX, startY};
    Position target = {targetX, targetY};

    cout << "Enter coordinates of " << numberOfZeroes << " blocked positions... " << endl;
    for (int i=0; i<numberOfZeroes; i++) {
        int x,y;
        cin >> x;
        cin >> y;
        mat[x][y] = '0';
    }

    cout << "-------------------------------" << endl;
    cout << "The matrix before finding the path: " << endl;
    display(mat);

    bool targetReached = reachTarget(mat, start, target);

    if (!targetReached) {
        cout << "Target is unreachable..." << endl;
    } else {
        cout << "The matrix after finding the path: " << endl;
        display(mat);
    }

    cout << "-------------------------------" << endl;

    return 0;
}

// Valid test input
/*
0 0
5 4
7
0 2
1 1
1 2
3 3
4 3
5 2
5 3
*/

// Invalid test input (unachievable target)
/*
0 0
4 4
11
0 2
1 1
1 2
3 3
3 4
3 5
4 3
4 5
5 2
5 3
5 4
*/
