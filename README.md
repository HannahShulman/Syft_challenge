# Syft coding challenge

## Tasks

There's a series of tasks to complete for the coding challenge.
Make sure you **add unit tests** for code that you write!
 
In no particular order, the tasks are:

#### 1. Add pagination - V
The source data comes from [here](https://jsonplaceholder.typicode.com/posts) so you can tweak the URL to read `?_page=2&_limit=20` (for example) to make the data paginated.

Bonus: leave some comments in the code around this specific way of paginating, and if there are any issues you can see by fetching "page 1", "page 2", and limiting.

#### 2. Animate new items coming in - V
New items being added to the recyclerview should animate in when a new page is loaded.

#### 3. Delete a post from the list - V
Add something to a list item that allows you to delete that item. The API will accept the `DELETE` verb, but subsequent fetches will return the item again; this will be expected when reviewing the test.

Bonus: animate the list item removal so that the other items move in to its place gracefully.

#### 4. Fix broken tests - V
There are some unit tests that are broken, fix them.

#### 5. (Bonus) Delete a post from the post details screen
The behaviour of this action is up to you. A couple of ideas could be:

1. Soft delete, and update the screen to tell the user the post way deleted. This should also be reflected in the list screen.
2. Delete the post from the database, kick the user off the screen, and remove the item from the recyclerview on the list screen, with a message saying it has been deleted.

## What we are looking for

Please don't spend more than 3 hours on the test, we understand that your time is important.

- Clear, easy to read, self-documenting code.
- Clear, concise, and easy to read unit tests.
- Consistency with existing code: Architecturally, semantically, and idiomatically, unless you believe there's a compelling reason to deviate. If so, please document it.
- Commit history: we should be able to follow how you approached the problem, what the iterations were, and how roughly long it took.
- Task completion.

Please provide any additional information that you want to communicate back to the reviewer here:

NOTES:
-This was a really interesting task for me for the following reasons:
a.First time I have tested Koin (and due to time limitation, I have commented out
                                a presenter test, if I have additional time, I will investigate and solve).
b.Haven't implemented MVP for at least 2 plus years.

-Never implements Pagination, was a new strategy for me to implement, hope I got that right.

c.Changes to code, although I would prob have  done some changes, I was focused on getting the tasks
done, and obviously keeping to the original project's language.

-Time spent: I hate to postpone tasks therefore implements last night,
 put into it approx 3 hours(3 plus with a break), and would love to hear from you, and get feedback on this one.
 (and I prob have some questions on the current implementation)



ORDER OF IMPLEMENTING TASKS:
1. Tests - first fix the broken tests. Broken tests mean that an expectation of
ours isn't working, therefore, either must change the expectation, or find out what the broken logic is.

2. Order of tasks, each task comes along with its test.

3. Pagination - See answer to bonus question as part of the documentation.

3. Delete post from list, I am aware of the fact that the behavior is a little wired,
this is due to not getting the correct data back from the server.
(I am not aure what you expected, having a more correct implementation, or having the behaviour right.
I chose the implementation, (could have fakely removed the item, from the adapter, then the items would
update nicely with an animation
In fact I would want to load the data directly from the db, however,
due to the db being updated with the full list, its a little hacky(returned data is a full list)

4. Unfortunately under the time limits, I was unable to complete the deletion from the detailed screen.
Had to many doubts of what the right way to implement would be, is it with onActivityResult?
observing the db data when resuming the calling activity
or maybe even working with a live data to reflect changes on the db?



