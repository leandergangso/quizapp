<script lang="ts">
	import { goto } from "$app/navigation"
	import { page } from "$app/stores"
	import { quizStore } from "$lib/stores/quiz.svelte"
	import { onMount } from "svelte"

	const paramKey = "step"

	let stepNr = $state(1)
	let progress = $state(0)

	function gotoPrev() {
		stepNr--
		if (stepNr < 0) {
			stepNr = 0
		}
		$page.url.searchParams.set(paramKey, stepNr.toString())
		goto($page.url, { replaceState: true })
	}

	async function gotoNext(evt: Event) {
		evt.preventDefault()
		if (stepNr >= quizStore.questions.length) {
			const ok = await quizStore.checkAnswers(evt)
			if (!ok) {
				return
			}
			goto("/answers", { replaceState: true })
			return
		}
		stepNr++
		$page.url.searchParams.set(paramKey, stepNr.toString())
		goto($page.url, { replaceState: true })
	}

	$effect(() => {
		stepNr = ($page.url.searchParams.get(paramKey) as number | null) ?? 1
		progress = Math.floor(((stepNr - 1) / quizStore.questions.length) * 100)
	})

	onMount(async () => {
		await quizStore.loadQuestions()
		let param = Number($page.url.searchParams.get(paramKey))
		if (isNaN(param) || param < 1 || param > quizStore.questions.length) {
			console.log(isNaN(param))
			$page.url.searchParams.set(paramKey, "1")
			goto($page.url, { replaceState: true })
		}
	})
</script>

<header>
	<div class="flex h-3 w-full items-center rounded-full border bg-gray-100">
		<div
			class="relative flex h-2.5 items-center rounded-full bg-blue-500"
			style="width: {progress}%"
		>
			<div
				class="absolute flex h-9 w-9 items-center justify-center rounded-full bg-blue-500 {progress == 0
					? '-right-9'
					: 'right-0'}"
			>
				<span class="text-xs font-semibold text-white">{progress}%</span>
			</div>
		</div>
	</div>
</header>

<main>
	<form
		onsubmit={gotoNext}
		class="space-y-8"
	>
		{#each quizStore.questions as question, idx}
			<div class:hidden={stepNr != idx + 1}>
				<h2>{question.title}</h2>
				{#if question.desc}
					<p>{question.desc}</p>
				{/if}

				<div class="space-y-4">
					<input
						type="hidden"
						value={question.id}
						name="questionID"
					/>
					{#each question.inputs as input}
						<label
							class="flex {input.type != 'TEXT'
								? 'flex-row-reverse items-center justify-end space-x-3 space-x-reverse'
								: 'flex-col'}"
						>
							<span>{input.label}</span>
							<input
								value={input.type != "TEXT" ? input.label : undefined}
								type={input.type}
								name={input.name}
							/>
						</label>
					{/each}
				</div>
			</div>
		{/each}

		<div class="flex space-x-6">
			<button
				type="button"
				disabled={stepNr <= 1}
				onclick={gotoPrev}>Prev</button
			>
			<button
				type="submit"
				class=" bg-blue-500 text-white"
			>
				{#if stepNr < quizStore.questions.length}
					Next
				{:else}
					Finish
				{/if}
			</button>
		</div>
	</form>
</main>
