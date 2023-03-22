# Tasks and Planning

### Task Identification

**Actionability:**

- Insufficient: User stories are used as issues, without breaking them down into smaller tasks.
- Good: User stories are broken down into multiple(!) concrete, technical tasks. These tasks are either modelled as GitLab issues (recommended) or they have been added as nested lists to GitLab issues.
- Excellent: User stories are broken down into tasks, which have are introduced with a short description (background/context/clarifications/...).

Grade: Good

Feedback:
> The issues are broken down into technical tasks, which is a good thing. However, they are inconsistently written amongst the team. I will elaborate more on how to write your issues a bit later in this feedback report.

**Traceability:**

- Insufficient: There is no visible connection between user stories and the project tasks.
- Good: It is clear how technical tasks relate to user stories.
- Excellent: Tags are used to make it easy to understand the source or nature of the various tasks (e.g., backlog, bug, extension, ...).

Grade: Excellent

**Granularity:**

- Insufficient: Tasks are either trivial or so big that they exceed the workload of individual developers.
- Good: Tasks seem to be reasonably sized. An average developer should be able to get each task done in an average work session of 3-4h.
- Excellent: The project history shows that team members are actually able to get multiple tasks done every week.

Grade: Excellent

**Expectations:**

- Insufficient: Tasks are ambiguous and their goals are not well defined.
- Sufficient: Most tasks have a descriptive title and a concrete goal.
- Good: Tasks contain a short description of the actions that need to be performed.
- Excellent: Tasks contain a short description of the actions that need to be performed and the expected outcomes/explicit acceptance criteria.

Grade: Good

Feedback:
> As I mentioned, your issues need to be more consistent amongst the team members. Some of the issues are already well written and the template is great, but for other the description is a single line.
> I will show you an example of an issue I wrote in one of my projects if you want a bit more inspiration
>> ### Description
>> The player needs to be notified when they want to leave the game. A pop-up needs to be shown, asking the player if they indeed want to exit the application or not.
>> ### Definition of done
>> This issue can be closed when the pop-up is correctly shown on every screen of the game. It should have two options: leaving the game or cancelling the action. In the case of cancelling, the game should continue normally.
>> ### Checklist
>> - [x] create functionality for displaying the pop-up
>> - [x] wire the pop-up to every screen in the game

> Moreover, here is a tutorial on how to integrate these issue/MR descriptions as a template (so you don't have to write them every time): https://docs.gitlab.com/ee/user/project/description_templates.html.


### Planning & Time Management

**Effort Estimation:**

- Insufficient: Required effort is not estimated for most tasks.
- Good: Efforts of tasks for the upcoming sprint get estimated.
- Excellent: Efforts of all tasks have an estimate, which is kept up-to-date in every sprint planning.

Grade: Insufficient

Feedback:
> You do not use the "Time tracking" feature of Gitlab issues. Here are the docs for this feature, pkease have a look over them: https://docs.gitlab.com/ee/user/project/time_tracking.html.

**Assignment Tracking:**

- Insufficient: Most tasks are not assigned to developers.
- Good: Tasks get assigned to developers.
- Excellent: Effort spent is tracked for most tasks.

Grade: Good

Feedback:
> Again, you do not track the spent effort. Please do so for the upcoming weeks :).

**Roadmap:**

- Insufficient: Planning is limited to the next week or not done at all.
- Good: Milestones and issues are used for planning future activities. Planning of the next milestone is updated weekly.
- Excellent: Efforts and estimates are taken into consideration in the planning. The overall plan is regularly updated.

Grade: (Very) Good

Feedback:
> Great that you work with sprints and that you already planned them beforehand!
> However, as you already noticed, estimates are crucial, please make sure you use them.

### Task Distribution

**Dedication:**

- Insufficient: A huge difference exists in how much time  the different developers dedicate to the project.
- Good: All developers contribute about the same amount of effort to the project.
- Excellent: Time spent is discussed in the weekly meetings. The team takes active counter measures to keep deviations reasonable.

Grade: Good

**Feature Isolation:**

- Insufficient: Tasks are developed on the main branch or feature branches receive direct commits from multiple developers.
- Good: Developers use feature branches to isolate their work from others while it is under development.
- Very Good: The latest changes on main are integrated into the feature branch before each MR (e.g., "merge main into feature" or "rebase feature onto main").
- Excellent: For collaborations of multiple developers, nested feature branches are used.

Grade: Good

**Areas of Expertise:**

- Insufficient: Developers only work in one area of the system, e.g., only on the frontend.
- Good: Developers have roles (e.g., frontend, backend, communication, ...), which are frequently rotated.
- Excellent: All developers constantly contribute to all system parts. Tasks a not distributed by area, but by feature, which usually results in cross-cutting changes.

Grade: Good

