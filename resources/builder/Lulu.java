package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Lulu extends _Default{
	public Lulu() {
		super("Lulu", "lu");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{3729};
				break;
			case HP:
				ids = new int[]{3732};
				break;
			case S1:
				ids = new int[]{3717, 3718};
				break;
			case S2:
				ids = new int[]{3725, 3726};
				break;
			case EX:
				ids = new int[]{6615, 6614};
				break;
			case AA:
				ids = new int[]{6609};
				break;
			case LD:
				ids = new int[]{};
				break;
			case BT:
				ids = new int[]{};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}