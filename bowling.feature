Feature: Bowling

Scenario: Domain Term Roll
Rule Roll is between 0 and 10 # ListOfObject ValueValid
| Value  | Valid  | Notes                  |
| 0      | true   |                        |
| 10     | true   |                        |
| 11     | false  |                        |
| -2     | false  |                        |
| TBR    | true   | Used for To Be Rolled  |

Data ValueValid
| Name   | Default  | DataType  |
| Value  | 0        | String    |
| Valid  | false    | Boolean   |
| Notes  |          | String    |


Scenario: Adding a roll 
Given rolls are                                 # ListOfListOfObject Roll
|  5 | 5  | 4  | 5  |  8 | 2  | 10  | 0 | 10 | 10 | 6 | 2| 10 | 4 | 6| 10 | 
When roll is                                    # ListOfListOfObject Roll
| 10 |
Then rolls become                               # ListOfListOfObject Roll
|  5 | 5  | 4  | 5  |  8 | 2  | 10  | 0 | 10 | 10 | 6 | 2| 10 | 4 | 6| 10 | 10 |

Scenario: Full Game Compute and Display
#Note:  one more roll remaining to end the game 
Given rolls are                                  # ListOfListOfObject Roll
|  5 | 5  | 4  | 5  |  8 | 2  | 10  | 0 | 10 | 10 | 6 | 2| 10 | 4 | 6| 10 | 10 |
When scored 
Then display is   
"""
|5/ |45 |8/ |X  |-/ |X  |62 |X  |4/ |XX |
| 14| 23| 43| 63| 83|101|109|129|149|   |
"""
# The above was done by "approval testing" - output was created, checked, and then inserted here

Define 
| Name  | Value  | Notes                     |
| TBR   | -1     | Roll has not occurred     |
| TBS   | -1     | Score not yet computable  |

Scenario: A Game in Steps 
Given rolls are                                  # ListOfListOfObject Roll
|  5 | 5  | 4  | 5  |  8 | 2  | 10  | 0 | 10 | 10 | 6 | 2| 10 | 4 | 6| 10 | 10 |
When scored 
Then frame values are                            # ListOfObject FrameValues Transpose
| Frame       | 1   | 2   | 3   | 4   | 5   | 6    | 7    | 8    | 9    | 10   |
| Roll1       | 5   | 4   | 8   | 10  | 0   | 10   | 6    | 10   | 4    | 10   |
| Roll2       | 5   | 5   | 2   | 0   | 10  | 6    | 2    | 4    | 6    | 10   |
| Roll3       | 4   | 8   | 10  | 10  | 10  | 2    | 10   | 6    | 10   | TBR  |
| Score       | 14  | 9   | 20  | 20  | 20  | 18   | 8    | 20   | 20   | TBS  |
| TotalScore  | 14  | 23  | 43  | 63  | 83  | 101  | 109  | 129  | 149  | TBS  |

Given frame values are as previous 
Then display values are                          # ListOfObject FrameDisplay Transpose 
| Frame       | 1   | 2   | 3   | 4   | 5   | 6    | 7    | 8    | 9    | 10   |
| Mark1       | 5   | 4   | 8   | X   | -   | X    | 6    | X    | 4    | X    |
| Mark2       | /   | 5   | /   |     | /   |      | 2    |      | /    | X    |
| Mark3       |     |     |     |     |     |      |      |      |      |      |
| TotalScore  | 14  | 23  | 43  | 63  | 83  | 101  | 109  | 129  | 149  |      |

Then game complete is                            # ListOfListOfObject Boolean 
| false| 

Then input control is                            # ListOfObject InputControlValues 
| Frame  | Roll  | Remaining  |
| 10     | 3     | 10         |


Scenario: Check for Game Complete 
Given rolls are                                  # ListOfListOfObject Roll
|  5 | 5  | 4  | 5  |  8 | 2  | 10  | 0 | 10 | 10 | 6 | 2| 10 | 4 | 6| 10 | 10 | 10 |
When scored 
Then game complete is                            # ListOfListOfObject Boolean 
| true | 


Scenario: Values for Tenth Frame 
Given rolls for tenth frame are                   # ListOfListOfObject Roll
| 10 | 10 | 
When scored
Then Then tenth frame values are                  # ListOfObject FrameValues Transpose
| Frame       | 10   |
| Roll1       | 10   |
| Roll2       | 10   |
| Roll3       | TBR  |
| Score       | TBS  |
| TotalScore  | TBS  |
# Could check for other tenth frame conditions 

Scenario: Input Control Should Be For Next Frame 
Given rolls are                                    # ListOfListOfObject Roll
| 10 | 
When scored 
Then input control is                              # ListOfObject InputControlValues 
| Frame  | Roll  | Remaining  |
| 2      | 1     | 10         |

Scenario: Try to add invalid roll 
Given rolls are                                 # ListOfListOfObject Roll
| 5 |
When scored 
When roll is                                    # ListOfListOfObject Roll
| 6 |
Then rolls become                               # ListOfListOfObject Roll
| 5 |


* Data FrameValues
| Name        | Default  | DataType  |
| Frame       | 1        | Integer   |
| Roll1       | TBR      | Roll      |
| Roll2       | TBR      | Roll      |
| Roll3       | TBR      | Roll      |
| Score       | TBS      | Score     |
| TotalScore  | TBS      | Score     |

Data FrameDisplay 
| Name        | Default  | DataType  | Notes                     |
| Frame       |          | String    |                           |
| Mark1       |          | String    |                           |
| Mark2       |          | String    |                           |
| Mark3       |          | String    | Only shown on 10th frame  |
| TotalScore  |          | String    |                           |

* Data InputControlValues 
| Name       | Default  | DataType  |
| Frame      | 1        | Integer   |
| Roll       | 1        | Integer   |
| Remaining  | 10       | Integer   |


